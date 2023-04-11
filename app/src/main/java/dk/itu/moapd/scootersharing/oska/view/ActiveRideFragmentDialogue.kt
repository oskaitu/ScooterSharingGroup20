package dk.itu.moapd.scootersharing.oska.view

import android.app.Dialog
import android.content.ContentValues
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Chronometer
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*


class ActiveRideFragmentDialogue : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val scooterToBeChanged = MainFragment.selectedScooter
        val view = View.inflate(context, dk.itu.moapd.scootersharing.oska.R.layout.fragment_active, null)
        val simpleChronometer = view.findViewById(dk.itu.moapd.scootersharing.oska.R.id.simpleChronometer) as Chronometer
        val startTime = System.currentTimeMillis()
        val startLocation = (activity as MainActivity).deviceLocation
        return activity?.let {
        val builder = AlertDialog.Builder(requireActivity())
            .setCancelable(false)
            .setTitle("Ride time")
            .setMessage("You are driving ${scooterToBeChanged._name} Vroom Vroom \n")
            .setPositiveButton("Stop driving") { _, _ ->
                try {
                    simpleChronometer.stop()
                    val endLocation = (activity as MainActivity).deviceLocation
                    var results = FloatArray(1)
                    var readableLocation =
                        convertCordsToAddress(endLocation.latitude, endLocation.longitude)
                    Location.distanceBetween(
                        startLocation.latitude,
                        startLocation.longitude,
                        endLocation.latitude,
                        endLocation.longitude,
                        results
                    )
                    val rideData = hashMapOf<String, Any>()
                    rideData["cost"] =
                        results[0].toInt() * simpleChronometer.base / 60 //cost is meters times minutes
                    rideData["end_time"] = System.currentTimeMillis()
                    rideData["scooterid"] = scooterToBeChanged._id
                    rideData["start_time"] = startTime
                    rideData["start_location"] =
                        "${startLocation.latitude}, ${startLocation.longitude}"
                    rideData["end_location"] = "${endLocation.latitude}, ${endLocation.longitude}"
                    rideData["distance"] = results[0].toInt()
                    val rentalData = hashMapOf<String, Any>()
                    rentalData["date"] = startTime
                    MainFragment.viewModel.addDocumentRentalAndRides(
                        rideData = rideData,
                        rentalData = rentalData
                    )
                    MainFragment.viewModel.updateDocument(
                        collection = "scooters",
                        item = MainFragment.selectedScooter._id,
                        fieldToUpdate = "location",
                        newData = "${endLocation.latitude}, ${endLocation.longitude}"
                    )
                    MainFragment.viewModel.updateDocument(
                        collection = "scooters",
                        item = MainFragment.selectedScooter._id,
                        fieldToUpdate = "translated_location",
                        newData = readableLocation
                    )
                    MainFragment.rider = false
                    MainFragment.selectedScooter = defaultScooter()

                } catch (e :IllegalArgumentException) //fix for oneplus specific error on this
                {
                    println(e.message)
                }
            }
            builder.setView(view)
            simpleChronometer.start()
            builder.create()
            builder.show()

        } ?: throw IllegalStateException("something exploded")

    }
    private fun convertCordsToAddress(latitude: Double, longitude: Double) : String {
        val geocoder = (activity as MainActivity).geocoder

        val addresses: List<Address>?
        val address: Address?
        var fulladdress = ""
        addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                address = addresses[0]
                fulladdress = address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
                var city = address.locality;
                var state = address.adminArea;
                var country = address.countryName;
                var postalCode = address.postalCode;
                var knownName = address.featureName; // Only if available else return NULL
            } else{
                fulladdress = "Location not found"
            }
        }
    return fulladdress
    }

    private fun Address.toAddressString() : String {
        val address = this

        // Create a `String` with multiple lines.
        val stringBuilder = StringBuilder()
        stringBuilder.apply {
            append(address.getAddressLine(0)).append("\n")
            append(address.postalCode).append(" ")
            append(address.locality).append("\n")
            append(address.countryName)
        }
        return stringBuilder.toString()
    }

}


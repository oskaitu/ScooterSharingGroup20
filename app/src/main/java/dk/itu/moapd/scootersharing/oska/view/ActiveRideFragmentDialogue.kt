package dk.itu.moapd.scootersharing.oska.view

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.model.Receipt
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue


class ActiveRideFragmentDialogue : DialogFragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var speedSensor : Sensor
    private var stepCount = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        /*sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)*/

        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        speedSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        sensorManager.registerListener(this, speedSensor, SensorManager.SENSOR_DELAY_NORMAL)

        val scooterToBeChanged = MainFragment.selectedScooter
        val view = View.inflate(context, dk.itu.moapd.scootersharing.oska.R.layout.fragment_active, null)

       /* val stepCountTextView = view.findViewById<TextView>(R.id.step_counter)
        stepCountTextView.text = stepCount.toString()*/


        val simpleChronometer = view.findViewById(dk.itu.moapd.scootersharing.oska.R.id.simpleChronometer) as Chronometer
        val startTime = System.currentTimeMillis()
        val startLocation = (activity as MainActivity).gps.getLocation()!!
        return activity?.let {
        val builder = AlertDialog.Builder(requireActivity())
            .setCancelable(false)
            .setTitle("Ride time")
            .setMessage("You are driving ${scooterToBeChanged._name} Vroom Vroom \n")
            .setPositiveButton("Stop driving") { _, _ ->
                try {
                    simpleChronometer.stop()
                    val endLocation = (activity as MainActivity).gps.getLocation()!!
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
                        15 + results[0].toInt()
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
                    MainFragment.mostRecentRide = Receipt(
                            name = "Newest",
                            endLocation = "",
                            endTime = System.currentTimeMillis(),
                            cost = 15 + results[0].toDouble(),
                            distance = results[0].toDouble(),
                            scooterName = MainFragment.selectedScooter._name,
                            startLocation = "-",
                            startTime = startTime)
                        MainFragment.selectedScooter = defaultScooter()
                    findNavController().navigate(R.id.paymentFragment)

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
    fun convertCordsToAddress(latitude: Double, longitude: Double) : String {
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
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val speed = event.values[0] * 3.6 // Convert m/s to km/h

            val speedTextView = dialog!!.findViewById<TextView>(R.id.step_counter)
            speedTextView.text = "${speed.toInt().absoluteValue} km/h"
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes here...
    }
    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }




}


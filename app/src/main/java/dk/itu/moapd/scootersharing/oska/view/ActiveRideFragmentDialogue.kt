package dk.itu.moapd.scootersharing.oska.view

import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Chronometer
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class ActiveRideFragmentDialogue : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val scooterToBeChanged = MainFragment.selectedScooter
        val view = View.inflate(context, dk.itu.moapd.scootersharing.oska.R.layout.fragment_active, null)
        val simpleChronometer = view.findViewById(dk.itu.moapd.scootersharing.oska.R.id.simpleChronometer) as Chronometer
        val startTime = System.currentTimeMillis()
        return activity?.let {
        val builder = AlertDialog.Builder(requireActivity())
            .setCancelable(false)
            .setTitle("Ride time")
            .setMessage("You are driving ${scooterToBeChanged._name} Vroom Vroom \n price ${simpleChronometer.base.compareTo(0)}")
            .setPositiveButton("Stop driving") { _, _ ->
                simpleChronometer.stop()
                val rideData = hashMapOf<String,Any>()
                    rideData["cost"] = 5 * simpleChronometer.base
                    rideData["distance"] = simpleChronometer.base*15  // temp until we work something out with distance #todo
                    rideData["end_time"] = System.currentTimeMillis()
                    rideData["scooterid"] = scooterToBeChanged._id
                    rideData["start_time"] = startTime
                val rentalData = hashMapOf<String, Any>()
                 rentalData["date"] = startTime

                MainFragment.viewModel.addDocumentRentalAndRides(rideData = rideData, rentalData = rentalData)
                MainFragment.rider = false
                MainFragment.selectedScooter = defaultScooter()
            }
            builder.setView(view)
            simpleChronometer.start()
            builder.create()
            builder.show()

        } ?: throw IllegalStateException("something exploded")

    }
}


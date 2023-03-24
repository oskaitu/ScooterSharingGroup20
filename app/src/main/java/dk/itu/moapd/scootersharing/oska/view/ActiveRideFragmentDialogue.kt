package dk.itu.moapd.scootersharing.oska.view

import android.R
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Chronometer
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment½


class ActiveRideFragmentDialogue : DialogFragment() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val scooterToBeChanged = MainFragment.selectedScooter
        val view = View.inflate(context, dk.itu.moapd.scootersharing.oska.R.layout.fragment_active, null)
        val simpleChronometer = view.findViewById(dk.itu.moapd.scootersharing.oska.R.id.simpleChronometer) as Chronometer
        var cost = 0
        return activity?.let {
        val builder = AlertDialog.Builder(requireActivity())
            .setTitle("Ride time")
            .setMessage("You are driving ${scooterToBeChanged._name} Vroom Vroom")
            .setPositiveButton("Stop driving") { _, _ ->
                dismiss()
                MainFragment.rider = false
                MainFragment.selectedScooter = defaultScooter()
                cost = 5 * simpleChronometer.base.compareTo(0)

            }
            .setNegativeButton("Pause") { _, _ ->
                simpleChronometer.stop()
            }


            builder.setView(view)
            simpleChronometer.start()
            builder.create()



        } ?: throw IllegalStateException("something exploded")

    }
}


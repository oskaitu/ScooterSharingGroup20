package dk.itu.moapd.scootersharing.oska.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Chronometer
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class ActiveRideFragmentDialogue : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val scooterToBeChanged = MainFragment.selectedScooter
        val view = View.inflate(context, dk.itu.moapd.scootersharing.oska.R.layout.fragment_active, null)
        val simpleChronometer = view.findViewById(dk.itu.moapd.scootersharing.oska.R.id.simpleChronometer) as Chronometer
        var cost = 0
        return activity?.let {
        val builder = AlertDialog.Builder(requireActivity())
            .setCancelable(false)
            .setTitle("Ride time")
            .setMessage("You are driving ${scooterToBeChanged._name} Vroom Vroom \n price ${simpleChronometer.base.compareTo(0)}")
            .setPositiveButton("Stop driving") { _, _ ->
                dismiss()
                MainFragment.rider = false
                MainFragment.selectedScooter = defaultScooter()
                simpleChronometer.stop()
                cost = 5 * simpleChronometer.base.compareTo(0)


            }


            builder.setView(view)
            simpleChronometer.start()
            builder.create()
            builder.show()



        } ?: throw IllegalStateException("something exploded")

    }

    override fun dismiss() {
        println("test")
    }
}


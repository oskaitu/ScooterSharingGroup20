package dk.itu.moapd.scootersharing.oska.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import dk.itu.moapd.scootersharing.oska.view.MainFragment
import dk.itu.moapd.scootersharing.oska.view.defaultScooter

/**
 * A simple [Fragment] subclass.
 * Use the [ActiveRideFragmentDialogue.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActiveRideFragmentDialogue : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var scooterToBeChanged = MainFragment.selectedScooter
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("hello")

            builder.setMessage("You are driving ${scooterToBeChanged._name} Vroom Vroom")
                .setPositiveButton("Stop driving") { h, _ ->
                    println("nonon")
                    Thread.sleep(5000)
                    h.dismiss()
                    //todo implement riding functionality
                }
                .setNegativeButton("Pause", DialogInterface.OnClickListener { _, _ -> //nothing
                })
            builder.create()

        } ?: throw IllegalStateException("something exploded")
    }
}

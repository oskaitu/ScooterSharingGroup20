package dk.itu.moapd.scootersharing.oska.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.view.MainFragment
import dk.itu.moapd.scootersharing.oska.view.defaultScooter

/**
 * A simple [Fragment] subclass.
 * Use the [StartRideFragmentDialogue.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartRideFragmentDialogue : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var scooterToBeChanged = MainFragment.selectedScooter
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Start driving ${scooterToBeChanged._name}?")
            builder.setMessage("Cost 50 \n Fuel 50 \n range 20 meters \n \n \n test")
                .setPositiveButton("Yes") { _, _ ->
                MainFragment.
                }
                .setNegativeButton("No", DialogInterface.OnClickListener { _, _ -> //nothing
                })
            builder.create()
        } ?: throw IllegalStateException("something exploded")
    }
}

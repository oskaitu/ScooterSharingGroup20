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
 * Use the [UpdateFragmentDialogue.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateFragmentDialogue : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var scooterToBeChanged = MainFragment.selectedScooter
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure you want to update ${scooterToBeChanged._name}?")
                .setPositiveButton("Yes") { _, _ ->
                    //todo update functionality
                }
                .setNegativeButton("No", DialogInterface.OnClickListener { _, _ -> //nothing
                })
            builder.create()
        } ?: throw IllegalStateException("something exploded")
    }
}

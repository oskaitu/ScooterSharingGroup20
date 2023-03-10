package dk.itu.moapd.scootersharing.oska

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmationFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var scooterToBeChanged = MainFragment.selectedScooter
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure you want to delete ${scooterToBeChanged._name}?")
                .setPositiveButton("Yes") { _, index ->
                    MainFragment.ridesDB.deleteSelectedScooter(scooterToBeChanged._name)
                    MainFragment.adapter.notifyItemRemoved(index)
                    MainFragment.selectedScooter= defaultScooter()
                }
                .setNegativeButton("No", DialogInterface.OnClickListener { _, _ -> //nothing
                })
            builder.create()
        } ?: throw IllegalStateException("something exploded")
    }
}

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
 * Confirmation that was previously used, it still contains the logic to delete scooters directly in the app
 * We have decided that that behavior is not fit for a program and we have no logic for checking admin users that
 * could potentially use it.
 */
class ConfirmationFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var scooterToBeChanged = MainFragment.selectedScooter

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure you want to delete ${scooterToBeChanged._name}?")
                .setPositiveButton("Yes") { _, index ->
                    MainFragment.viewModel.deleteDocument("scooters", scooterToBeChanged._id)
                    MainFragment.adapter.notifyItemRemoved(index)
                    MainFragment.selectedScooter = defaultScooter()
                }
                .setNegativeButton("No", DialogInterface.OnClickListener { _, _ -> //nothing
                })
            builder.create()
        } ?: throw IllegalStateException("something exploded")
    }
}

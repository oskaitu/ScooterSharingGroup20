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
 * A fragment used previously to show receipts, it was replaced by paymentfragment
 */
class UpdateFragmentDialogue : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var scooterToBeChanged = MainFragment.selectedScooter
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Ride ended for ${MainFragment.mostRecentRide.name}?")
            builder.setMessage("Total Cost ${MainFragment.mostRecentRide.cost}")
                .setPositiveButton("Pay") { _, _ ->
                    MainFragment.viewModel.updateDocument(
                        "scooters",
                        scooterToBeChanged._id,
                        "timestamp",
                        System.currentTimeMillis()
                    )
                }
                .setNegativeButton("No", DialogInterface.OnClickListener { _, _ -> //nothing

                })
            builder.create()
        } ?: throw IllegalStateException("something exploded")
    }
}

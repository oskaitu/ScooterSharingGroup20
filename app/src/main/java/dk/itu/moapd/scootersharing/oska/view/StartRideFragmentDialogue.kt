package dk.itu.moapd.scootersharing.oska.view

import android.app.Dialog
import android.content.DialogInterface
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.scootersharing.oska.R

/**
 * A Fragment used to display info to a user as a dialogue when they are clicked on the map or in the list
 * it also contains the logic for navigating to the qr scanner
 */
class StartRideFragmentDialogue : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var scooterToBeChanged = MainFragment.selectedScooter
        val yourloc = (activity as MainActivity).getLocationInGoodFormat()
        val scooterloc = scooterToBeChanged._location.trim().split(",")
        val results = FloatArray(5)
        Location.distanceBetween(
            yourloc[0], yourloc[1], scooterloc[0].toDouble(), scooterloc[1].toDouble(),
            results
        )
        val random = (Math.random() * 100).toInt()
        var distance = ""
        if (results[0].toInt() < 1000) {
            distance = "${results[0].toInt()} meters away"
        } else {
            distance = "${(results[0] / 1000).toInt()} km away"
        }
        return activity?.let {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Start driving ${scooterToBeChanged._name}?")
            builder.setMessage(
                "${scooterToBeChanged._translated_location}" +
                        "\n" +
                        "${random}% battery left" +
                        "\n" +
                        distance
            )
                .setPositiveButton("Scan QR") { _, _ ->
                    findNavController().navigate((R.id.fragment_scanner))
                }
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                })

            println("did the builder")
            builder.create()
            builder.show()


        } ?: throw IllegalStateException("something exploded")


    }

}

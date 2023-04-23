package dk.itu.moapd.scootersharing.oska.view

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Chronometer
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.view.MainFragment
import dk.itu.moapd.scootersharing.oska.view.defaultScooter
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass.
 * Use the [StartRideFragmentDialogue.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartRideFragmentDialogue : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var scooterToBeChanged = MainFragment.selectedScooter

        return activity?.let {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Start driving ${scooterToBeChanged._name}?")
            builder.setMessage("Cost 50 \n Fuel 50 \n range 20 meters")
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

package dk.itu.moapd.scootersharing.oska.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class MlKitFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(requireActivity())
            var text = ""
            MainFragment.labels.forEach { text+=it }
            builder.setTitle("Found following items on picture")

            builder.setMessage(text)

                .setPositiveButton("Cool") { _, _ ->
                }
            builder.create()
            builder.show()


        } ?: throw IllegalStateException("something exploded")


    }

}
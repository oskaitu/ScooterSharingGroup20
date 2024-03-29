package dk.itu.moapd.scootersharing.oska

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.FragmentUpdateRideBinding


class UpdateRideFragment : Fragment() {
    companion object {
        lateinit var ridesDB : RidesDB
        var selectedScooter : Scooter = Scooter("error","error",System.currentTimeMillis())
    }

    private lateinit var _binding: FragmentUpdateRideBinding
    private val binding
        get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUpdateRideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        ridesDB = RidesDB.get(requireContext())

        with (binding) {
            UpdateRideButton.setOnClickListener { view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)

                if(editTextName.text.isNotEmpty() &&
                    editLocationName.text.isNotEmpty()){
                    val location = editLocationName.text.toString().trim()
                    selectedScooter._location = location
                    selectedScooter._timestamp = System.currentTimeMillis()

                    Snackbar.make(view, "Ride started using scooter = ${binding.editTextName.text}, " +
                            "On location ${binding.editLocationName.text} ", Snackbar.LENGTH_SHORT).show()

                    editTextName.text.clear()
                    editLocationName.text.clear()
                    showMessage("updated Ride fragment successfully")

                } else {
                    Snackbar.make(view, R.string.invalid_information, Snackbar.LENGTH_SHORT).show()
                    showMessage("something went bad in fragment update")
                }

            }
        }

    }
    private fun showMessage (message : String) {
    // Print a message in the ‘Logcat ‘ system .
         Log.d( TAG , message)
     }



}
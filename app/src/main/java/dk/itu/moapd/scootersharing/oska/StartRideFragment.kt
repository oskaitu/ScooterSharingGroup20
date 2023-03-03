package dk.itu.moapd.scootersharing.oska

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

import dk.itu.moapd.scootersharing.oska.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.oska.databinding.FragmentStartRideBinding
import java.util.*


class StartRideFragment : Fragment() {
    companion object {
        lateinit var ridesDB : RidesDB
        private lateinit var adapter: CustomArrayAdapter
        var selectedScooter : Scooter = Scooter("error","error",System.currentTimeMillis())
    }

    private lateinit var _binding: FragmentStartRideBinding
    private val binding
        get() = checkNotNull(_binding) {

        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStartRideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        ridesDB = RidesDB.get(requireContext())

        with (binding) {
            RideButton.setOnClickListener { view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)

                if(editTextName.text.isNotEmpty() &&
                    editLocationName.text.isNotEmpty()){
                    val name = editTextName.text.toString().trim()
                    val location = editLocationName.text.toString().trim()
                    selectedScooter._location = location
                    selectedScooter._timestamp = System.currentTimeMillis()

                    ridesDB.addScooter(name,location)
                    Snackbar.make(view, "Ride started using scooter = ${binding.editTextName.text}, " +
                            "On location ${binding.editLocationName.text} ", Snackbar.LENGTH_SHORT).show()

                    editTextName.text.clear()
                    editLocationName.text.clear()

                } else {
                    Snackbar.make(view, R.string.invalid_information, Snackbar.LENGTH_SHORT).show()
                }

            }
        }

    }

}
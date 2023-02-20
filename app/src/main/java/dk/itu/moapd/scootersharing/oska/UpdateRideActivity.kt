package dk.itu.moapd.scootersharing.oska

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.widget.Button
import android.widget.EditText
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.ActivityUpdateRideBinding


class UpdateRideActivity : AppCompatActivity() {
    /*companion object {
        private val TAG = MainActivity::class.qualifiedName
    }*/

    private lateinit var binding: ActivityUpdateRideBinding

    private val scooter: Scooter = Scooter ("", "",0)

    override fun onCreate(savedInstanceState: Bundle?) {
        //WindowCompat.setDecorFitsSystemWindows(window , false )
        super.onCreate(savedInstanceState)

        // Edit texts .
        binding = ActivityUpdateRideBinding.inflate(layoutInflater)

        setContentView(binding.root)

        with (binding) {
            UpdateRideButton.setOnClickListener(){ view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)

                if(editTextName.text.isNotEmpty() &&
                    editLocationName.text.isNotEmpty()){
                    val name = editTextName.text.toString().trim()
                    val location = editLocationName.text.toString().trim()
                    scooter._name = name
                    scooter._location = location
                    scooter._timestamp = System.currentTimeMillis()

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
    /* private fun showMessage () {
 // Print a message in the ‘Logcat ‘ system .
         Log.d( TAG , scooter.toString())
     }*/



}
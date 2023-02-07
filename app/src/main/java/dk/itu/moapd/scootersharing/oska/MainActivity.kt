package dk.itu.moapd.scootersharing.oska

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // A set of private constants used in this class .
    companion object {
        private val TAG = MainActivity :: class.qualifiedName
    }
    // GUI variables .
    private lateinit var binding : ActivityMainBinding
    private lateinit var workableBinding : ActivityMainBinding

    private val scooter : Scooter = Scooter ("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        workableBinding = ActivityMainBinding.bind(binding.root)
        setContentView(binding.root)


        with(workableBinding) {


            registerButton.setOnClickListener { view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)

                if (scooterName.text.isNotEmpty() &&
                    locationName.text.isNotEmpty()
                ) {
                    // Update the object attributes .
                    val name = scooterName.text.toString().trim()
                    val location = locationName.text.toString().trim()
                    scooter._name = name
                    scooter._location = location
                    Snackbar.make(
                        registerButton,
                        R.string.valid_scooter,
                        Snackbar.LENGTH_SHORT
                    )
                        .show()

                    // Reset the text fields and update the UI.
                    scooterName.text.clear()
                    locationName.text.clear()
                    showMessage()
                } else {
                    var text = getAPILevel()
                    welcomeScreenText.text = text
                    Snackbar.make(
                        registerButton,
                        R.string.invalid_scooter,
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }


        }

    }
    private fun showMessage () {
// Print a message in the ‘Logcat ‘ system .
        Log.d (TAG,scooter.toString())
    }

    private fun getAPILevel () :String {
        return buildString {
        append("API level ")
        append(Build.VERSION.SDK_INT)
    }
    }

}
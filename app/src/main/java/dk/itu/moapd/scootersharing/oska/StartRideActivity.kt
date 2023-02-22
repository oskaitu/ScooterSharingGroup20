package dk.itu.moapd.scootersharing.oska

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.ActivityMainBinding
import dk.itu.moapd.scootersharing.oska.databinding.ActivityStartRideBinding

class StartRideActivity : AppCompatActivity() {


    companion object {
        private val TAG = StartRideActivity::class.qualifiedName
    }

    // GUI variables .
    private lateinit var binding: ActivityStartRideBinding
    private lateinit var workableBinding: ActivityStartRideBinding

    private val scooter: Scooter = Scooter("", "",0)

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_ride)

        binding = ActivityStartRideBinding.inflate(layoutInflater)
        workableBinding = ActivityStartRideBinding.bind(binding.root)
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
                    scooter._location = location
                    scooter._timestamp = System.currentTimeMillis()
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
    private fun showMessage() {
// Print a message in the ‘Logcat ‘ system .
        Log.d(TAG, scooter.toString())
    }

}

package dk.itu.moapd.scootersharing.oska

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.ActivityUpdateRideBinding

class UpdateRideActivity : AppCompatActivity() {

    companion object {
        private val TAG = UpdateRideActivity::class.qualifiedName
    }
    private lateinit var binding: ActivityUpdateRideBinding
    private lateinit var workableBinding: ActivityUpdateRideBinding

    private val scooter: Scooter = Scooter("", "", 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_ride)
        binding = ActivityUpdateRideBinding.inflate(layoutInflater)
        workableBinding = ActivityUpdateRideBinding.bind(binding.root)
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
                    //todo implement an overview for scooters to update the right location
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

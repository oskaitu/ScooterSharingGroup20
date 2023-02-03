package dk.itu.moapd.scootersharing.oska

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    private lateinit var scooterName: EditText
    private lateinit var scooterLocation: EditText
    private lateinit var startRideButton: Button

    private val scooter: Scooter = Scooter ("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window , false )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Edit texts .
        scooterName = findViewById(R.id.editTextName)
        scooterLocation = findViewById(R.id.editLocationName)

        // Buttons .
        startRideButton = findViewById(R.id.ButtonClicker)
        startRideButton.setOnClickListener {
            if (scooterName.text.isNotEmpty() &&
                scooterLocation.text.isNotEmpty() ) {
        // Update the object attributes .

                val name = scooterName.text.toString().trim()
                scooter.setName(name)
                val location = scooterLocation.text.toString().trim()
                scooter.setLocation(location)
                Snackbar.make(startRideButton, R.string.correct_information, Snackbar.LENGTH_SHORT).show()

                // Reset the text fields and update the UI.
                scooterName.text.clear()
                scooterLocation.text.clear()
                showMessage()
            } else {
                Snackbar.make(startRideButton, R.string.invalid_information, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    private fun showMessage () {
// Print a message in the ‘Logcat ‘ system .
        Log.d( TAG , scooter.toString())
    }



}
package dk.itu.moapd.scootersharing.oska

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    // A set of private constants used in this class .
    companion object {
        private val TAG = MainActivity :: class.qualifiedName
    }
    // GUI variables .
    private lateinit var scooterName : EditText
    private lateinit var scooterLocation : EditText
    private lateinit var startRideButton : Button
    private lateinit var infoText : TextView

    private val scooter : Scooter = Scooter ("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat . setDecorFitsSystemWindows ( window , false )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Edit texts .
        scooterName = findViewById ( R.id.scooter_name)
        scooterLocation = findViewById( R.id.location_name)

        // Buttons .
        startRideButton = findViewById(R.id.register_button)

        //info
        infoText = findViewById(R.id.welcome_screen_text)

        startRideButton . setOnClickListener {
            if ( scooterName.text.isNotEmpty() &&
                scooterLocation.text.isNotEmpty()) {
            // Update the object attributes .
                val name = scooterName.text.toString().trim()
                val location = scooterLocation.text.toString().trim()
                scooter.setName(name)
                scooter.setLocation(location)
                Snackbar.make(
                    startRideButton,
                    R.string.valid_scooter,
                    Snackbar.LENGTH_SHORT)
                    .show()

            // Reset the text fields and update the UI.
                scooterName.text.clear()
                scooterLocation.text.clear()
                showMessage()
            } else
            {
                var text = getAPILevel()
                infoText.text = text
                Snackbar.make(
                    startRideButton,
                    R.string.invalid_scooter,
                    Snackbar.LENGTH_SHORT)
                    .show()
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
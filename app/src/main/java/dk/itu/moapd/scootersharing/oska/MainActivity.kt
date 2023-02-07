package dk.itu.moapd.scootersharing.oska

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.widget.Button
import android.widget.EditText
import androidx.core.view.WindowCompat
import dk.itu.moapd.scootersharing.oska.databinding.ActivityMainBinding

import com.google.android.material.snackbar.Snackbar

/**
 * MainActivity
 *
 * This class represents the main activity of the scooter sharing application. It contains the logic for
 * handling user input and displaying the relevant information to the user.
 *
 * @property mainBinding The binding object for the activity main layout.
 * @property binding The binding object for the activity layout.
 * @property scooter The scooter object that will be shared.
 *
 */
class MainActivity : AppCompatActivity() {
    /*companion object {
        private val TAG = MainActivity::class.qualifiedName
    }*/

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var binding: ActivityMainBinding
    private val scooter: Scooter = Scooter ("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        //WindowCompat.setDecorFitsSystemWindows(window , false )
        super.onCreate(savedInstanceState)

        // Edit texts .
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        binding = ActivityMainBinding.bind(mainBinding.root)

        setContentView(mainBinding.root)

        with (binding) {
             ButtonClicker.setOnClickListener(){ view ->
                 view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)

                 if(editTextName.text.isNotEmpty() &&
                     editLocationName.text.isNotEmpty()){
                     val name = editTextName.text.toString().trim()
                     val location = editLocationName.text.toString().trim()
                     scooter._name = name
                     scooter._location = location

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
package dk.itu.moapd.scootersharing.oska

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.view.WindowCompat
import dk.itu.moapd.scootersharing.oska.databinding.ActivityMainBinding

import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

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
                 if(editTextName.text.isNotEmpty() &&
                     editLocationName.text.isNotEmpty()){
                     var name = binding.editTextName.text
                     var location = binding.editLocationName.text
                     Snackbar.make(view, R.string.correct_information, Snackbar.LENGTH_SHORT).show()
                     binding.editTextName.text = name
                     binding.editLocationName.text = location
                 } else {
                     Snackbar.make(view, R.string.invalid_information, Snackbar.LENGTH_SHORT).show()
                 }
             }
        }

    }
    private fun showMessage () {
// Print a message in the ‘Logcat ‘ system .
        Log.d( TAG , scooter.toString())
    }



}
package dk.itu.moapd.scootersharing.oska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.widget.Button
import android.widget.EditText
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.ActivityMainBinding

/**
 * MainActivity
 *
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //WindowCompat.setDecorFitsSystemWindows(window , false )
        super.onCreate(savedInstanceState)

        // Edit texts .

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        with (binding) {
            StartRideButton.setOnClickListener(){ view ->
                val intent = Intent(view.context, StartRideActivity::class.java)
                startActivity(intent)

                 }
            UpdateRideButton.setOnClickListener(){
                val intent = Intent(this.root.context, UpdateRideActivity::class.java)
                startActivity(intent)

            }

        }
    }

}





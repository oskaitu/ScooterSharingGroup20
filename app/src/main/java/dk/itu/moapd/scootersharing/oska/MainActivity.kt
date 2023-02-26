package dk.itu.moapd.scootersharing.oska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.ActivityMainBinding
import kotlin.math.log

/**
 * MainActivity
 *
 *
 */
public class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var ridesDB : RidesDB
        private lateinit var adapter: CustomArrayAdapter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        //WindowCompat.setDecorFitsSystemWindows(window , false )
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        ridesDB = RidesDB.get(this)
        var list = ridesDB.getRidesList()

        adapter = CustomArrayAdapter(this, R.layout.scooter_list_item, list)

        binding.scooterList.adapter = adapter


        //binding.scooterList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, a)
       // binding.scooterList.adapter = ArrayAdapter(this, R.layout.scooter_list_item, R.id.scooter_name_item, list)

        setContentView(binding.root)

        with (binding) {
            StartRideButton.setOnClickListener(){ view ->
                val intent = Intent(view.context, StartRideActivity::class.java)
                startActivity(intent)

                 }
            UpdateRideButton.setOnClickListener(){view ->
                val intent = Intent(view.context, UpdateRideActivity::class.java)
                startActivity(intent)

            }




        }
    }

}





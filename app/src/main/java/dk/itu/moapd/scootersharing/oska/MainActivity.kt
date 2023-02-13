/**
 * MIT License

Copyright (c) [2023] [Nicolai Seloy / Oscar Kankanranta]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

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
                    Snackbar.make(
                        registerButton,
                        R.string.invalid_scooter,
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
            APIButton.setOnClickListener { view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                if(APIversion.text.equals(""))
                {
                    var text = getAPILevel()
                    APIversion.text = text
                } else {
                    APIversion.text = ""
                }


            }


        }

    }

    /**
     * Our logger function
     */
    private fun showMessage () {
// Print a message in the ‘Logcat ‘ system .
        Log.d (TAG,scooter.toString())
    }

    /**
     * Returns device current android version
     * @return String containing version number
     */
    private fun getAPILevel () :String {
        return buildString {
        append("API level ")
        append(Build.VERSION.SDK_INT)
    }
    }

}
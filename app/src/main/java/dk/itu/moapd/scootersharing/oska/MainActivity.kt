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

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // GUI variables .
    private lateinit var binding : ActivityMainBinding
    private lateinit var workableBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        workableBinding = ActivityMainBinding.bind(binding.root)
        setContentView(binding.root)


        with(workableBinding) {
            registerButton.setOnClickListener { view ->
                val intent = Intent(view.context,StartRideActivity::class.java)
                startActivity(intent)
            }
            updateButton.setOnClickListener { view ->
                val intent = Intent(view.context,UpdateRideActivity::class.java)
                startActivity(intent)
            }

            APIButton.setOnClickListener { view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                if (APIversion.text.equals("")) {
                    val text = getAPILevel()
                    APIversion.text = text
                } else {
                    APIversion.text = ""
                }


            }


        }
    }

    private fun getAPILevel () :String {
        return buildString {
        append("API level ")
        append(Build.VERSION.SDK_INT)
    }
    }

}
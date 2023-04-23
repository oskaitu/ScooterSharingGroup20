package dk.itu.moapd.scootersharing.oska.view

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.ActivityMainBinding
import dk.itu.moapd.scootersharing.oska.viewModel.LocationService
import dk.itu.moapd.scootersharing.oska.viewModel.MainActivityVM
import org.opencv.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.core.Mat
import java.util.*

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
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


   lateinit var deviceLocation : Location
    //lateinit var deviceLocation2 : Location
    lateinit var geocoder : Geocoder
    lateinit var gps: LocationService

    private lateinit var auth: FirebaseAuth



    companion object{
        private const val ALL_PERMISSIONS_RESULT = 1337
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


    }

    private val db = Firebase.firestore

    private var newUser = true

    private val settings: FirebaseFirestoreSettings = firestoreSettings {
        cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        isPersistenceEnabled = true
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build())


        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.ic_launcher_round)
            .setTheme(R.style.Theme_ScooterShare)
            .setIsSmartLockEnabled(false)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null)
        {
            createSignInIntent()
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding = ActivityMainBinding.inflate(layoutInflater)


        geocoder = Geocoder(this,Locale.getDefault())
        setContentView(binding.root)


        requestUserPermissions()
        startLocationAware()

    }

    fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_signout]
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            db.firestoreSettings = settings
            val user = FirebaseAuth.getInstance().currentUser

            val tasks = db.collection("user").get()

            tasks.addOnSuccessListener { collection ->
                for (document in collection){
                    //DO NOT CHANGE THIS
                        if(user?.email!!.equals(document.get("email"))){
                            newUser = false
                        }
                    }
                }
            tasks.addOnCompleteListener {
                if(newUser)
                {
                    val data = hashMapOf(
                        "authref" to user.toString(),
                        "name" to user?.displayName,
                        "email" to user?.email
                    )
                    db.collection("user")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            println("DocumentSnapshot written with ID: ${documentReference.id} b")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding document ${e.message}")
                        }
                }}



        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
    private fun startLocationAware() {

        // Show a dialog to ask the user to allow the application to access the device's location.
        requestUserPermissions()

        gps = LocationService(this, this.getSystemService(LOCATION_SERVICE) as LocationManager)
        if(checkPermission())
        {
            deviceLocation = gps.getLocation()!!
        }


    }




    private fun requestUserPermissions() {

        // An array with location-aware permissions.
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissions.add(Manifest.permission.CAMERA)




        // Check which permissions is needed to ask to the user.
        val permissionsToRequest = permissionsToRequest(permissions)

        // Show the permissions dialogs to the user.
        if (permissionsToRequest.size > 0)
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                ALL_PERMISSIONS_RESULT
            )
    }


    private fun permissionsToRequest(permissions: ArrayList<String>): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                result.add(permission)
        return result
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if the user has accepted the permissions to access the camera.
        if (requestCode == REQUEST_CODE_PERMISSIONS)
            if (allPermissionsGranted())
                //startCamera()

            // If permissions are not granted, present a toast to notify the user that the
            // permissions were not granted.
            else {
                //snackBar("Permissions not granted by the user.")
                finish()
            }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) != PackageManager.PERMISSION_GRANTED

}








package dk.itu.moapd.scootersharing.oska.view

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.ActivityMainBinding
import dk.itu.moapd.scootersharing.oska.viewModel.LocationService
import dk.itu.moapd.scootersharing.oska.viewModel.MainActivityVM
import dk.itu.moapd.scootersharing.oska.viewModel.OpenCVUtils
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType.CV_8UC4
import org.opencv.core.Mat
import java.text.SimpleDateFormat
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
class MainActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityVM by lazy {
        ViewModelProvider(this)[MainActivityVM::class.java]
    }
    /**
     * A callback from OpenCV Manager to handle the OpenCV library.
     */
    private lateinit var loaderCallback: BaseLoaderCallback

    /**
     * The OpenCV image storage.
     */
    private lateinit var imageMat: Mat

    /**
     * The camera characteristics allows to select a camera or return a filtered set of cameras.
     */
    private var cameraCharacteristics = CameraCharacteristics.LENS_FACING_BACK

    /**
     * A variable to control the image analysis method to apply in the input image.
     */
    private var currentMethodId = 0


    //private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    //private lateinit var locationCallback: LocationCallback
    lateinit var deviceLocation : Location
    lateinit var deviceLocation2 : Location
    lateinit var geocoder : Geocoder
    lateinit var gps: LocationService


    companion object{
        private const val ALL_PERMISSIONS_RESULT = 1337
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)


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
            AuthUI.IdpConfig.GoogleBuilder().build())


        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.ic_launcher_round)
            .setIsSmartLockEnabled(false)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }

    override fun onStart() {
        createSignInIntent()
        super.onStart()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)

        cameraCharacteristics =
            viewModel.characteristics.value ?: CameraCharacteristics.LENS_FACING_BACK
        viewModel.characteristics.observe(this) {
            cameraCharacteristics = it
        }
        currentMethodId =
            viewModel.methodId.value ?: 0
        viewModel.methodId.observe(this) {
            currentMethodId = it
        }
        if (allPermissionsGranted())
            startCamera()
        else
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)

        // Define the UI behavior.
        binding.cameraContent.apply {

            // Listener for button used to switch cameras.
            cameraSwitchButton.setOnClickListener {
                viewModel.onCameraCharacteristicsChanged(
                    if (CameraCharacteristics.LENS_FACING_FRONT == cameraCharacteristics)
                        CameraCharacteristics.LENS_FACING_BACK
                    else
                        CameraCharacteristics.LENS_FACING_FRONT
                )

                // Re-start use cases to update selected camera.
                cameraView.disableView()
                cameraView.setCameraIndex(cameraCharacteristics)
                cameraView.enableView()
            }

            // Listener for button used to change the image analysis method.
            imageAnalysisButton.setOnClickListener {
                var methodId = currentMethodId + 1
                methodId %= 4
                viewModel.onMethodChanged(methodId)
            }
        }
        geocoder = Geocoder(this,Locale.getDefault())
        setContentView(binding.root)



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

        /*
        // Start receiving location updates.
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        // Initialize the `LocationCallback`.
        locationCallback = object : LocationCallback() {

            /**
             * This method will be executed when `FusedLocationProviderClient` has a new location.
             *
             * @param locationResult The last known location.
             */
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                // Updates the user interface components with GPS data location.
                locationResult.lastLocation?.let { location ->
                    deviceLocation = deviceLocation2
                    println(deviceLocation2)
                    println(deviceLocation)
                }
            }
        }

         */
    }




    private fun requestUserPermissions() {

        // An array with location-aware permissions.
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

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
                startCamera()

            // If permissions are not granted, present a toast to notify the user that the
            // permissions were not granted.
            else {
                snackBar("Permissions not granted by the user.")
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
                ) != PackageManager.PERMISSION_GRANTED

    override fun onResume() {
        super.onResume()

        // Try to initialize OpenCV using the newest init method. Otherwise, use the asynchronous
        // one.
        if (!OpenCVLoader.initDebug())
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,
                this, loaderCallback)
        else
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
    }
    override fun onPause() {
        super.onPause()
        binding.cameraContent.cameraView.disableView()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.cameraContent.cameraView.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        // Create the OpenCV Mat structure to represent images in the library.
        imageMat = Mat(height, width, CV_8UC4)
    }

    /**
     * This method is invoked when camera preview has been stopped for some reason. No frames will
     * be delivered via `onCameraFrame()` callback after this method is called.
     */
    override fun onCameraViewStopped() {
        imageMat.release()
    }

    /**
     * This method is invoked when delivery of the frame needs to be done. The returned values - is
     * a modified frame which needs to be displayed on the screen.
     *
     * @param inputFrame The current frame grabbed from the video camera device stream.
     */
    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {

        // Get the current frame and copy it to the OpenCV Mat structure.
        val image = inputFrame?.rgba()
        imageMat = image!!

        if (cameraCharacteristics == CameraCharacteristics.LENS_FACING_BACK)
            Core.flip(image, image, 1)

        return when (currentMethodId) {
            1 -> OpenCVUtils.convertToGrayscale(image)
            2 -> OpenCVUtils.convertToBgra(image)
            3 -> OpenCVUtils.convertToCanny(image)
            else -> image
        }
    }

    /**
     * This method is used to start the video camera device stream.
     */
    private fun startCamera() {

        // Setup the OpenCV camera view.
        binding.cameraContent.cameraView.apply {
            visibility = SurfaceView.VISIBLE
            setCameraIndex(cameraCharacteristics)
            setCameraPermissionGranted()
            setCvCameraViewListener(this@MainActivity)
        }

        // Initialize the callback from OpenCV Manager to handle the OpenCV library.
        loaderCallback = object : BaseLoaderCallback(this) {
            override fun onManagerConnected(status: Int) {
                when (status) {
                    SUCCESS -> binding.cameraContent.cameraView.enableView()
                    else -> super.onManagerConnected(status)
                }
            }
        }
    }


    /**
     * Make a standard toast that just contains text.
     *
     * @param text The text to show. Can be formatted text.
     * @param duration How long to display the message. Either `Toast.LENGTH_SHORT` or
     *      `Toast.LENGTH_LONG`.
     */
    private fun snackBar(text: CharSequence,
                         duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar
            .make(findViewById(R.id.camera_content), text, duration)
            .show()
    }

/*
    private fun subscribeToLocationUpdates() {

        // Check if the user allows the application to access the location-aware resources.
        if (checkPermission())
            return

        // Sets the accuracy and desired interval for active location updates.
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, 1000).build()
            

        // Subscribe to location changes.
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

 */
    /*

    private fun unsubscribeToLocationUpdates() {
        // Unsubscribe to location changes.
        fusedLocationProviderClient
            .removeLocationUpdates(locationCallback)
    }

    private fun Long.toDateString() : String {
        val date = Date(this)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
    override fun onPause() {
        super.onPause()
        unsubscribeToLocationUpdates()
    }


     */





}








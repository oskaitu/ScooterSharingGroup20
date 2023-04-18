package dk.itu.moapd.scootersharing.oska.view

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.FragmentCameraBinding
import dk.itu.moapd.scootersharing.oska.view.MainActivity.Companion.REQUEST_CODE_PERMISSIONS
import dk.itu.moapd.scootersharing.oska.view.MainActivity.Companion.REQUIRED_PERMISSIONS
import dk.itu.moapd.scootersharing.oska.viewModel.MainActivityVM
import dk.itu.moapd.scootersharing.oska.viewModel.OpenCVUtils
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat


class CameraFragment : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var _binding: FragmentCameraBinding

    private lateinit var loaderCallback: BaseLoaderCallback

    private lateinit var imageMat: Mat

    private var cameraCharacteristics = CameraCharacteristics.LENS_FACING_BACK

    private val viewModel: MainActivityVM by lazy {
        ViewModelProvider(this)[MainActivityVM::class.java]
    }

    /**
     * A variable to control the image analysis method to apply in the input image.
     */
    private var currentMethodId = 0

    private val binding
        get() = checkNotNull(_binding) {

        }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraCharacteristics =
            viewModel.characteristics.value ?: CameraCharacteristics.LENS_FACING_BACK
        viewModel.characteristics.observe((activity as MainActivity)) {
            cameraCharacteristics = it
        }
        currentMethodId =
            viewModel.methodId.value ?: 0
        viewModel.methodId.observe((activity as MainActivity)) {
            currentMethodId = it
        }

        //if (checkPermission())
            startCamera()
        /*else
            ActivityCompat.requestPermissions(requireActivity(),
                REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS )*/




        binding.apply {

            // Listener for button used to switch cameras.
            fragmentCameraSwitchButton.setOnClickListener {
                viewModel.onCameraCharacteristicsChanged(
                    if (CameraCharacteristics.LENS_FACING_FRONT == cameraCharacteristics)
                        CameraCharacteristics.LENS_FACING_BACK
                    else
                        CameraCharacteristics.LENS_FACING_FRONT
                )

                // Re-start use cases to update selected camera.
                fragmentCameraView.disableView()
                fragmentCameraView.setCameraIndex(cameraCharacteristics)
                fragmentCameraView.enableView()
            }

            // Listener for button used to change the image analysis method.
            fragmentImageAnalysisButton.setOnClickListener {
                var methodId = currentMethodId + 1
                methodId %= 4
                viewModel.onMethodChanged(methodId)
            }
            fragmentCameraCaptureButton.setOnClickListener{
                println("capture")
            }
        }
    }


    override fun onResume() {
        super.onResume()

        OpenCVLoader.initDebug()
        // Try to initialize OpenCV using the newest init method. Otherwise, use the asynchronous
        // one.
        /*if (!OpenCVLoader.initDebug())
            OpenCVLoader.initAsync(
                OpenCVLoader.OPENCV_VERSION,
                this.activity, loaderCallback)
        else
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)*/
    }
    override fun onPause() {
        super.onPause()
        binding.fragmentCameraView.disableView()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.fragmentCameraView.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        // Create the OpenCV Mat structure to represent images in the library.
        imageMat = Mat(height, width, CvType.CV_8UC4)
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
        //OpenCVLoader.initDebug()

        // Setup the OpenCV camera view.
        binding.fragmentCameraView.apply {
            visibility = SurfaceView.VISIBLE
            setCameraIndex(cameraCharacteristics)
            setCameraPermissionGranted()
            setCvCameraViewListener(this@CameraFragment)
        }


        // Initialize the callback from OpenCV Manager to handle the OpenCV library.
        binding.fragmentCameraView.enableView()
    }
    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if the user has accepted the permissions to access the camera.
        if (requestCode == REQUEST_CODE_PERMISSIONS)
            if (allPermissionsGranted())
            startCamera()

            // If permissions are not granted, present a toast to notify the user that the
            // permissions were not granted.
            else {
                //snackBar("Permissions not granted by the user.")
                //finish()
            }
    }*/
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }











}



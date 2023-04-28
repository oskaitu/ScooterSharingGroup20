package dk.itu.moapd.scootersharing.oska.view

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.camera2.CameraCharacteristics
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.google.android.material.snackbar.Snackbar
import com.google.rpc.Code
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.FragmentScannerBinding
import dk.itu.moapd.scootersharing.oska.view.MainActivity.Companion.REQUIRED_PERMISSIONS
import dk.itu.moapd.scootersharing.oska.viewModel.MainActivityVM
import dk.itu.moapd.scootersharing.oska.viewModel.OpenCVUtils
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.objdetect.QRCodeDetector
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * A fragment containing the QR scanner, implemented using OpenCV
 */
class ScannerFragment : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var _binding: FragmentScannerBinding
    private lateinit var scanner: QRCodeDetector


    private lateinit var imageMat: Mat

    private var cameraCharacteristics = CameraCharacteristics.LENS_FACING_FRONT

    private val viewModel: MainActivityVM by lazy {
        ViewModelProvider(this)[MainActivityVM::class.java]
    }


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
        _binding = FragmentScannerBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cameraCharacteristics =
            viewModel.characteristics.value ?: CameraCharacteristics.LENS_FACING_FRONT
        viewModel.characteristics.observe((activity as MainActivity)) {
            cameraCharacteristics = it
        }


        startCamera()


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
        binding.fragmentScannerView.disableView()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.fragmentScannerView.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        // Create the OpenCV Mat structure to represent images in the library.
        imageMat = Mat(height, width, CvType.CV_8UC4)
        scanner = QRCodeDetector()

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
    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat? {

        /*
        This try catch is to handle E/cv::error(): OpenCV(4.7.0-dev)
        We rarely get an error called A/libc: Fatal signal 11 (SIGSEGV), code 2 (SEGV_ACCERR), fault addr 0x7d945c2340 in tid 25372 (Thread-4), pid 23865 (tersharing.oska)
        seems to be an active issue on github
        https://github.com/opencv/opencv/issues/21532
        and it has no effect on us
         */
        try {


            // Get the current frame and copy it to the OpenCV Mat structure.
            val image = inputFrame?.rgba()

            imageMat = image!!

            if (cameraCharacteristics == CameraCharacteristics.LENS_FACING_BACK)
                Core.flip(image, image, 1)

            if (MainFragment.selectedScooter._id == scanner.detectAndDecode(imageMat)) {
                parentFragmentManager.popBackStack()

                MainFragment.rider = true
                Snackbar.make(
                    binding.root.rootView,
                    scanner.detectAndDecode(imageMat),
                    Snackbar.LENGTH_SHORT
                ).show()

            }

            return image
        } catch (e: Exception) {
            println(e.message)
        }
        return null
    }


    /**
     * This method is used to start the video camera device stream.
     */
    private fun startCamera() {
        //OpenCVLoader.initDebug()

        // Setup the OpenCV camera view.
        binding.fragmentScannerView.apply {
            visibility = SurfaceView.VISIBLE
            setCameraIndex(cameraCharacteristics)
            setCameraPermissionGranted()
            setCvCameraViewListener(this@ScannerFragment)
        }


        // Initialize the callback from OpenCV Manager to handle the OpenCV library.
        binding.fragmentScannerView.enableView()
    }

}



package dk.itu.moapd.scootersharing.oska.viewModel

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc



/**
 * An utility class to manage a set of image analysis algorithms.
 */
object OpenCVUtils {

    /**
     * Convert the input image to a grayscale image.
     *
     * @param image The input image in the RGBA color space.
     *
     * @return A transformed image in the grayscale color space.
     */
    fun convertToGrayscale(image: Mat?): Mat {
        val grayscale = Mat()
        Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_RGBA2GRAY)
        return grayscale
    }

    /**
     * Convert the input image to a BGRA image.
     *
     * @param image The input image in the RGBA color space.
     *
     * @return A transformed image in the BGRA color space.
     */
    fun convertToBgra(image: Mat?): Mat {
        val bgra = Mat()
        Imgproc.cvtColor(image, bgra, Imgproc.COLOR_RGBA2BGRA)
        return bgra
    }

    /**
     * Apply a Canny filter in the input image.
     *
     * @param image The input image in the RGBA color space.
     *
     * @return A filtered image with a Canny filter.
     */
    fun convertToCanny(image: Mat?): Mat {

        // Canny filter requires a grayscale image as the input image.
        val grayscale = convertToGrayscale(image)

        // Apply an automatic threshold to create a binary image.
        val thresh = Mat()
        val otsuThresh = Imgproc.threshold(grayscale, thresh,
            0.0, 255.0, Imgproc.THRESH_BINARY or Imgproc.THRESH_OTSU)

        // Apply the Canny filter.
        val canny = Mat()
        Imgproc.Canny(grayscale, canny, otsuThresh * 0.5, otsuThresh)

        // Release the unused OpenCV resources.
        grayscale.release()
        thresh.release()

        // Return the filtered image.
        return canny
    }

}
package dk.itu.moapd.scootersharing.oska.view

import android.content.Context
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dk.itu.moapd.scootersharing.oska.databinding.FragmentGeolocationBinding
import java.text.SimpleDateFormat
import java.util.*


class GeolocationFragment : Fragment() {

    private lateinit var _binding: FragmentGeolocationBinding


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val binding
        get() = checkNotNull(_binding) {

        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return binding.root


    }

    /*override fun onPause() {
        super.onPause()
        unsubscribeToLocationUpdates()
    }
    override fun onResume() {
        super.onResume()
        subscribeToLocationUpdates()
    }*/

    private fun startLocationAware() {


        // Start receiving location updates.
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireContext())

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
                    updateUI(location)
                }
            }
        }
    }
    private fun updateUI(location: Location) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.apply {
                latitudeTextField?.editText?.setText(location.latitude.toString())
                longitudeTextField?.editText?.setText(location.longitude.toString())
                timeTextField?.editText?.setText(location.time.toDateString())
            }
        else{

        }
        //setAddress(location.latitude, location.longitude)
    }

    /*private fun setAddress(latitude: Double, longitude: Double) {
        if (!Geocoder.isPresent())
            return

        // Create the `Geocoder` instance.
        val geocoder = Geocoder(this, Locale.getDefault())

        // After `Tiramisu Android OS`, it is needed to use a listener to avoid blocking the main
        // thread waiting for results.
        val geocodeListener = Geocoder.GeocodeListener { addresses ->
            addresses.firstOrNull()?.toAddressString()?.let { address ->
                binding.addressTextField?.editText?.setText(address)
            }
        }

        // Return an array of Addresses that attempt to describe the area immediately surrounding
        // the given latitude and longitude.
        if (Build.VERSION.SDK_INT >= 33)
            geocoder.getFromLocation(latitude, longitude, 1, geocodeListener)
        else
            geocoder.getFromLocation(latitude, longitude, 1)?.let {  addresses ->
                addresses.firstOrNull()?.toAddressString()?.let { address ->
                    binding.contentMain.addressTextField?.editText?.setText(address)
                }
            }
    }*/

    private fun Long.toDateString() : String {
        val date = Date(this)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }




}
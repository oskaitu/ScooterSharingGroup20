package dk.itu.moapd.scootersharing.oska.view

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
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
import dk.itu.moapd.scootersharing.oska.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.util.*


class GeolocationFragment : Fragment() {

    private lateinit var _binding: FragmentGeolocationBinding

    private val binding
        get() = checkNotNull(_binding) {

        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeolocationBinding.inflate(inflater, container, false)
        super.onCreate(savedInstanceState)
        return binding.root


    }
    override fun onStart() {
        super.onStart()
        updateUI((activity as MainActivity).gps.getLocation()!!)
    }
    private fun updateUI(location: Location) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)

            binding.apply {
                latitudeTextField?.editText?.setText(location.latitude.toString())
                longitudeTextField?.editText?.setText(location.longitude.toString())
                timeTextField?.editText?.setText(location.time.toDateString())
                //addressTextField?.editText?.setText(setAddress(location.latitude, location.longitude))
            }
        else{

        }
        setAddress(location.latitude, location.longitude)
    }

    private fun setAddress(latitude: Double, longitude: Double) {
        val geocoder = (activity as MainActivity).geocoder

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
                    binding.addressTextField?.editText?.setText(address)
                }
            }
    }

    private fun Long.toDateString() : String {
        val date = Date(this)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
    private fun Address.toAddressString() : String {
        val address = this

        // Create a `String` with multiple lines.
        val stringBuilder = StringBuilder()
        stringBuilder.apply {
            append(address.getAddressLine(0)).append("\n")
            append(address.postalCode).append(" ")
            append(address.locality).append("\n")
            append(address.countryName)
        }

        return stringBuilder.toString()
    }




}
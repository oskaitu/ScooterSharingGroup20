package dk.itu.moapd.scootersharing.oska.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.FragmentMapBinding
import dk.itu.moapd.scootersharing.oska.model.Scooter
import dk.itu.moapd.scootersharing.oska.viewModel.ScooterViewModel


class MapFragment : Fragment() {

    private lateinit var _binding: FragmentMapBinding
    private val db = Firebase.firestore



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
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        super.onCreate(savedInstanceState)


        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment?

        supportMapFragment?.getMapAsync { googleMap ->

            if ((activity as MainActivity).checkPermission())
                return@getMapAsync

            // Show the current device's location as a blue dot.
            googleMap.isMyLocationEnabled = true

            // Set the default map type.
            //googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL


            db.collection("scooters")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w(ContentValues.TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }


                    for (doc in value!!) {
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(parseLatLngFromString(doc.get("location") as String))
                                .title(doc.get("name") as String)

                        )
                    }
                }

            val cph = LatLng(55.676098, 12.568337)
            googleMap.addMarker(
                MarkerOptions()
                    .position(cph)
                    .title("Marker in Sydney")
            )






            // Setup the UI settings state.
            googleMap.uiSettings.apply {
                isCompassEnabled = true
                isIndoorLevelPickerEnabled = true
                isMyLocationButtonEnabled = true
                isRotateGesturesEnabled = true
                isScrollGesturesEnabled = true
                isTiltGesturesEnabled = true
                isZoomControlsEnabled = true
                isZoomGesturesEnabled = true
            }
            googleMap.setPadding(0, 100, 0, 0)

        }



        return binding.root
    }

    fun parseLatLngFromString(latLonString: String): LatLng {
        val latLon = latLonString.split(",") // Split string by comma
        val lat = latLon[0].toDouble() // Convert latitude string to double
        val lon = latLon[1].toDouble() // Convert longitude string to double
        return LatLng(lat, lon) // Return a new LatLng object
    }




}



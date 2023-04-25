package dk.itu.moapd.scootersharing.oska.view

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.FragmentMapBinding
import dk.itu.moapd.scootersharing.oska.model.Scooter
import kotlinx.coroutines.selects.select


class MapFragment : Fragment() {

    private lateinit var _binding: FragmentMapBinding
    private val db = Firebase.firestore



    private val binding
        get() = checkNotNull(_binding) {

        }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(MainFragment.rider)
        {
            findNavController().navigate(R.id.activeFragment)
        }
        super.onViewCreated(view, savedInstanceState)
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
            val copenhagen = LatLng(55.6761, 12.5683)
            val cameraPosition = CameraPosition.Builder()
                .target(copenhagen)
                .zoom(12f)
                .build()
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

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
                                .title(doc.id)
                                .icon(BitmapFromVector(requireContext(),R.drawable.logo_scooter))
                        )
                    }
                }
            googleMap.setOnMarkerClickListener {marker ->
                val docRef = db.collection("scooters").document(marker.title!!)
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            MainFragment.selectedScooter =
                                Scooter(
                                    _id = document.id,
                                    _name = document.get("name") as String,
                                    _location = document.get("location") as String,
                                    _timestamp = document.get("timestamp") as Long,
                                    _translated_location = document.get("translated_location") as String?
                                )
                            //Toast.makeText(requireContext(), "Selected  ${MainFragment.selectedScooter._name}", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.startFragment)
                        }
                    }
                true
            }


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

    //source https://www.geeksforgeeks.org/how-to-add-custom-marker-to-google-maps-in-android/
    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return fromBitmap(bitmap)
    }




}



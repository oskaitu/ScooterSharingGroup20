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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromAsset
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.FragmentMapBinding


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
                                .icon(BitmapFromVector(requireContext(),R.drawable.logo_scooter))
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



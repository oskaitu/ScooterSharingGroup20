package dk.itu.moapd.scootersharing.oska.view


import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.oska.model.Receipt
import dk.itu.moapd.scootersharing.oska.model.Scooter
import dk.itu.moapd.scootersharing.oska.viewModel.RecyclerViewAdapter
import dk.itu.moapd.scootersharing.oska.viewModel.ScooterViewModel

class MainFragment : Fragment() {

    var _binding: FragmentMainBinding? = null
    private val db = Firebase.firestore

    /**
     * This property is only valid between `onCreateView()` and `onDestroyView()` methods.
     */
    private val binding
        get() = checkNotNull(_binding) {

        }

    companion object {
        val receipts = mutableListOf<Receipt>()
        lateinit var circularProgressDrawable : CircularProgressDrawable
        lateinit var adapter: RecyclerViewAdapter
        lateinit var viewModel : ScooterViewModel
        lateinit var storage: FirebaseStorage
        lateinit var storageRef : StorageReference
        var selectedScooter : Scooter = defaultScooter()
        var rider = false
        var labels = mutableListOf<String>()
        val user = FirebaseAuth.getInstance().currentUser


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ScooterViewModel::class.java)
        storage = Firebase.storage("gs://moapd-2023-8c62e.appspot.com/")
        storageRef = storage.reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){



        adapter = RecyclerViewAdapter(viewModel)
        circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        with (binding) {
            //recyclerView.visibility= View.INVISIBLE

            if(rider)
            {
                findNavController().navigate(R.id.activeFragment)
            }

            StartRideButton.setOnClickListener {
                if(selectedScooter._name!="error")
                {
                    if(rider)
                    {
                        findNavController().navigate(R.id.activeFragment)


                    }else
                    {
                        findNavController().navigate(
                            R.id.startFragment)
                    }

                } else
                {
                    Snackbar.make(it,"You need to select a scooter first!", Snackbar.LENGTH_SHORT).show()
                }
            }

            APIButton.setOnClickListener { view ->

                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                    if (APIversion.text.equals("")) {
                        val text = getAPILevel()
                        APIversion.text = text
                        showMessage("printing api version ")
                    } else {
                        APIversion.text = ""
                    }
            }
            DeleteRideButton.setOnClickListener {
                receipts.clear()
                if(selectedScooter._name=="error")
                {
                    db.collection("rental_history")
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                Log.w(ContentValues.TAG, "Listen failed.", error)
                                return@addSnapshotListener
                            }

                            val history = mutableListOf<String>()
                            for (doc in value!!) {
                                if (doc.get("accountid") as String == (activity as MainActivity).auth.currentUser?.email) {
                                    history.add(

                                        doc.get("ridesid") as String
                                    )
                                }
                            }
                            if (history.size != 0) {

                                db.collection("rides")
                                    .addSnapshotListener { value, error ->
                                        if (error != null) {
                                            Log.w(ContentValues.TAG, "Listen failed.", error)
                                            return@addSnapshotListener
                                        }
                                        while (history.isNotEmpty()) {
                                            var check = history.removeLast()
                                            for (doc in value!!) {
                                                if (doc.id == check) {
                                                }
                                                    var scoot = doc.get("scooterid") as String?
                                                    if(scoot != null)
                                                    {
                                                        val docRef = db.collection("scooters").document(scoot)
                                                        docRef.get()
                                                            .addOnSuccessListener { document ->
                                                                if (document != null) {
                                                                    val name = document.get("name") as String
                                                    receipts.add(
                                                        Receipt(
                                                            name = doc.id,
                                                            startTime = doc.get("start_time") as Long,
                                                            endTime = doc.get("end_time") as Long,
                                                            startLocation = doc.get("start_location") as String,
                                                            endLocation = doc.get("end_location") as String,
                                                            distance = doc.get("distance") as Number,
                                                            cost = doc.get("cost") as Number,
                                                            scooterName = name
                                                        )
                                                    ) }
                                                                findNavController().navigate(R.id.receiptFragment)
                                                            } } } } } } } }
                else findNavController().navigate(R.id.confirmationFragment) }

            UpdateRideButton.setOnClickListener {
                findNavController().navigate(R.id.updateFragment)

            }
           ShowListButton.setOnClickListener {


               findNavController().navigate(R.id.available_scooter_recyclerview)
            }
            Gotolocation.setOnClickListener{
                if(!(activity as MainActivity).checkPermission())
                {
                    findNavController().navigate((R.id.fragment_geolocation))
                }else
                    Snackbar.make(it,"You need to enable locations in App settings", Snackbar.LENGTH_SHORT).show()
            }
            Gotomap.setOnClickListener{
                if(!(activity as MainActivity).checkPermission())
                {
                    println((activity as MainActivity).checkPermission())

                    findNavController().navigate((R.id.fragment_map))
                }else
                    Snackbar.make(it,"You need to enable locations in App settings", Snackbar.LENGTH_SHORT).show()
            }
            Gotocamera.setOnClickListener{
                println((activity as MainActivity).checkPermission())
                    findNavController().navigate((R.id.fragment_camera))

            }
            Gotoscanner.setOnClickListener{
                println((activity as MainActivity).checkPermission())
                findNavController().navigate((R.id.fragment_scanner))

            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAPILevel () :String {
        return buildString {
            append("API level ")
            append(Build.VERSION.SDK_INT)
        }
    }

    private fun showMessage (message : String) {
        // Print a message in the ‘Logcat ‘ system .
        Log.d(ContentValues.TAG, message)
    }

}

 fun defaultScooter () : Scooter {
    return Scooter("error","error","error",System.currentTimeMillis(),"")
}




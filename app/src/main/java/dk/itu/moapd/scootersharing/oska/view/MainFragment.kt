package dk.itu.moapd.scootersharing.oska.view


import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
import dk.itu.moapd.scootersharing.oska.model.Scooter
import dk.itu.moapd.scootersharing.oska.viewModel.RecyclerViewAdapter
import dk.itu.moapd.scootersharing.oska.viewModel.ScooterViewModel

class MainFragment : Fragment() {

    private val scooterCollectionRef = Firebase.firestore.collection("scooters")

    var list = ArrayList<Scooter>()


    var _binding: FragmentMainBinding? = null


    /**
     * This property is only valid between `onCreateView()` and `onDestroyView()` methods.
     */
    private val binding
        get() = checkNotNull(_binding) {

        }

    companion object {
        lateinit var adapter: RecyclerViewAdapter
        lateinit var viewModel : ScooterViewModel
        lateinit var storage: FirebaseStorage
        lateinit var storageRef : StorageReference
        //this is pretty cursed, but we need a mutable type and we just need to get around not having the error error showing up but showing the user something if they manage to do it
        var selectedScooter : Scooter = defaultScooter()

        var rider = false
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

        val database = (activity as MainActivity).db


        //list.add(Scooter("test","test",12345))

        /*database.collection("scooters")
            .get()
            .addOnSuccessListener { result ->
            for (document in result) {
                list.add(Scooter(
                    _name = document.get("name") as String,
                    _location = document.get("location") as String,
                    _timestamp = document.get("timestamp") as Long)

                )
                println("addded scooter til list with ${document.id} as ID and ${document.data}")
            }
        }
            .addOnFailureListener { exception ->
                println( "Error getting documents. ${exception.message}")
            }*/

       /* adapter = RecyclerViewAdapter(viewModel)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.loadData()*/
        //binding.scooterList.adapter = adapter

        with (binding) {
            //recyclerView.visibility= View.INVISIBLE

            StartRideButton.setOnClickListener {
                if(selectedScooter._name!="error")
                {
                    if(rider)
                    {
                        findNavController().navigate(R.id.activeFragment)


                    }else
                    {
                        rider=true
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
                if(selectedScooter._name=="error") Snackbar.make(it,"You need to select a scooter first!", Snackbar.LENGTH_SHORT).show()
                else findNavController().navigate(R.id.confirmationFragment) }

            UpdateRideButton.setOnClickListener {
                findNavController().navigate(R.id.updateFragment)

            }

            LogoutButton.setOnClickListener{
                (activity as MainActivity).signOut()
                (activity as MainActivity).createSignInIntent()

            }
           ShowListButton.setOnClickListener {

               findNavController().navigate(R.id.available_scooter_recyclerview)

               /*adapter.notifyDataSetChanged()
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                list.iterator().forEach { println("scooter name is ${it._name}") }
                if(recyclerView.isInvisible){
                    recyclerView.visibility= View.VISIBLE
                    showMessage("showing scooterlist")
                } else {
                    recyclerView.visibility=View.INVISIBLE
                    showMessage("hiding scooterlist")

                }*/
            }

            /*scooterList.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
                selectedScooter = ridesDB.getCurrentScooter(i)!!
                showMessage("found ${selectedScooter._name}")
                Snackbar.make(
                    scooterList,
                    ridesDB.getCurrentScooterInfo(i), Snackbar.LENGTH_SHORT
                ).show()
            }*/
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
    fun startDriving () {
        findNavController().navigate(R.id.activeFragment)
    }

    private fun showMessage (message : String) {
        // Print a message in the ‘Logcat ‘ system .
        Log.d(ContentValues.TAG, message)
    }


}

 fun defaultScooter () : Scooter {
    return Scooter("error","error","error",System.currentTimeMillis())
}




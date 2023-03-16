package dk.itu.moapd.scootersharing.oska.view


import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.RidesDB
import dk.itu.moapd.scootersharing.oska.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.oska.model.Scooter
import dk.itu.moapd.scootersharing.oska.viewModel.RecyclerViewAdapter

class MainFragment : Fragment() {


    private var _binding: FragmentMainBinding? = null

    /**
     * This property is only valid between `onCreateView()` and `onDestroyView()` methods.
     */
    private val binding
        get() = checkNotNull(_binding) {

        }

    companion object {
        lateinit var ridesDB : RidesDB
        public lateinit var adapter: RecyclerViewAdapter
        //this is pretty cursed, but we need a mutable type and we just need to get around not having the error error showing up but showing the user something if they manage to do it
        var selectedScooter : Scooter = defaultScooter()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){


        ridesDB = RidesDB.get(requireContext())
        var list = ridesDB.getRidesList()

        adapter = RecyclerViewAdapter(list)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        //binding.scooterList.adapter = adapter

        with (binding) {

            recyclerView.visibility= View.INVISIBLE

            StartRideButton.setOnClickListener {
                findNavController().navigate(
                    R.id.show_startride_fragment
                )
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
                findNavController().navigate(R.id.show_update_fragment)

            }

            LogoutButton.setOnClickListener{
                (activity as MainActivity).signOut()
                (activity as MainActivity).createSignInIntent()

            }
            ShowListButton.setOnClickListener { view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                if(recyclerView.isInvisible){
                    recyclerView.visibility= View.VISIBLE
                    showMessage("showing scooterlist")
                } else {
                    recyclerView.visibility=View.INVISIBLE
                    showMessage("hiding scooterlist")

                }
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

    private fun showMessage (message : String) {
        // Print a message in the ‘Logcat ‘ system .
        Log.d(ContentValues.TAG, message)
    }




}

 fun defaultScooter () : Scooter {
    return Scooter("error","error",System.currentTimeMillis())
}


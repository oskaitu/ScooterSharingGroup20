package dk.itu.moapd.scootersharing.oska


import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.FragmentMainBinding

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
        private lateinit var adapter: CustomArrayAdapter
        //this is pretty cursed, but we need a mutable type and we just need to get around not having the error error showing up but showing the user something if they manage to do it
        var selectedScooter : Scooter = Scooter("error","error",System.currentTimeMillis())
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){


        ridesDB = RidesDB.get(requireContext())
        var list = ridesDB.getRidesList()

        adapter = CustomArrayAdapter(requireContext(), R.layout.scooter_list_item, list)

        binding.scooterList.adapter = adapter

        with (binding) {

            scooterList.visibility= View.INVISIBLE

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
                    for (i in 0 until ridesDB.getRidesList().size)
                    {
                        if (ridesDB.getRidesList()[i] == selectedScooter)
                        {
                            ridesDB.deleteSelectedScooter(i)
                            adapter.notifyDataSetChanged()
                            showMessage("deleting $i from list")
                            break
                        }}
            }

            UpdateRideButton.setOnClickListener {
                findNavController().navigate(R.id.show_update_fragment)

            }
            ShowListButton.setOnClickListener { view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                if(scooterList.isInvisible){
                    scooterList.visibility= View.VISIBLE
                    showMessage("showing scooterlist")
                } else {
                    scooterList.visibility=View.INVISIBLE
                    showMessage("hiding scooterlist")

                }
            }
            scooterList.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
                selectedScooter = ridesDB.getCurrentScooter(i)!!
                showMessage("found ${selectedScooter._name}")
                Snackbar.make(
                    scooterList,
                    ridesDB.getCurrentScooterInfo(i), Snackbar.LENGTH_SHORT
                ).show()
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





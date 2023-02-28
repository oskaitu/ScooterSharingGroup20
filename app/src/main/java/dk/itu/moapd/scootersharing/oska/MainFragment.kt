package dk.itu.moapd.scootersharing.oska

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isInvisible
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // GUI variables .
    private var _binding : FragmentMainBinding? = null
    private var _workableBinding : FragmentMainBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val workableBinding
        get() = checkNotNull(_workableBinding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    companion object {
        lateinit var ridesDB : RidesDB
        private lateinit var adapter: ScooterListAdapter
        var selectedScooter : Scooter = Scooter("error","error",System.currentTimeMillis())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ridesDB = RidesDB.get(requireContext())
        adapter = ScooterListAdapter(requireContext(),R.layout.list_item_scooter, ridesDB.getRidesList())

        binding.scooterList.adapter = adapter
        with(workableBinding) {
            scooterList.visibility= View.INVISIBLE
            registerButton.setOnClickListener { view ->
                val intent = Intent(view.context,StartRideActivity::class.java)
                startActivity(intent)
            }
            updateButton.setOnClickListener { view ->
                val intent = Intent(view.context,UpdateRideActivity::class.java)
                startActivity(intent)
            }

            APIButton.setOnClickListener { view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                if (APIversion.text.equals("")) {
                    val text = getAPILevel()
                    APIversion.text = text
                } else {
                    APIversion.text = ""
                }

            }
            ScooterListButton.setOnClickListener { view ->
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                if(scooterList.isInvisible){
                    scooterList.visibility=View.VISIBLE
                } else {
                    scooterList.visibility=View.INVISIBLE
                }
            }
            scooterList.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
                MainActivity.selectedScooter = ridesDB.getCurrentScooter(i)!!
                Snackbar.make(
                    scooterList,
                    ridesDB.getCurrentScooterInfo(i)
                    , Snackbar.LENGTH_SHORT
                ).show()

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _workableBinding = null
    }


    private fun getAPILevel () :String {
        return buildString {
            append("API level ")
            append(Build.VERSION.SDK_INT)
        }
    }

}
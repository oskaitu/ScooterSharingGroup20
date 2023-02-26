package dk.itu.moapd.scootersharing.oska

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dk.itu.moapd.scootersharing.oska.databinding.FragmentScooterListBinding

private const val TAG = "ScooterListFragment"
class ScooterListFragment : Fragment() {

    private var _binding: FragmentScooterListBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val ScooterListViewModel: MainActivity.ScooterListViewModel by viewModels()

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total Scooters:' ${ScooterListViewModel.scooters.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScooterListBinding.inflate(inflater, container, false)
        binding.scooterRecyclerView.layoutManager = LinearLayoutManager(context)

        val scooters = ScooterListViewModel.scooters
        val adapter = ScooterListAdapter(scooters)
        binding.scooterRecyclerView.adapter = adapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
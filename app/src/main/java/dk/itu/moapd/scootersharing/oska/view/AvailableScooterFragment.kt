package dk.itu.moapd.scootersharing.oska.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.FragmentAvailableScooterBinding
import dk.itu.moapd.scootersharing.oska.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.oska.model.Scooter
import dk.itu.moapd.scootersharing.oska.viewModel.RecyclerViewAdapter
import dk.itu.moapd.scootersharing.oska.viewModel.ScooterViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class AvailableScooterFragment : Fragment() {

    private var _binding : FragmentAvailableScooterBinding? = null
    private val binding
        get() = checkNotNull(_binding) {

        }
    companion object {
        public lateinit var adapter: RecyclerViewAdapter

        lateinit var viewModel : ScooterViewModel

    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        _binding = FragmentAvailableScooterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ScooterViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RecyclerViewAdapter(viewModel)

        binding.availableScooterRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.availableScooterRecyclerview.adapter = adapter

        viewModel.loadData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
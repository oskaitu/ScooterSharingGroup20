package dk.itu.moapd.scootersharing.oska

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.oska.databinding.ListItemScooterBinding

class ScooterHolder (
    val binding: ListItemScooterBinding
)   : RecyclerView.ViewHolder(binding.root) {
}

class ScooterListAdapter(
    private val scooter: List<Scooter>)
    :RecyclerView.Adapter<ScooterHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScooterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemScooterBinding.inflate(inflater, parent, false)
        return  ScooterHolder(binding)
    }

    override fun getItemCount(): Int {
       return scooter.size
    }

    override fun onBindViewHolder(holder: ScooterHolder, position: Int) {
        val scooter = scooter[position]
        holder.apply {
            binding.scooterName.text = scooter._name
            binding.scooterDate.text = scooter._timestamp.toString()
        }
    }
}
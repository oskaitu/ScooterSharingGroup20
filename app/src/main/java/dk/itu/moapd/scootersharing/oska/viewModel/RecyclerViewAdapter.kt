package dk.itu.moapd.scootersharing.oska.viewModel

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.oska.view.MainFragment
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.databinding.CardviewitemBinding
import dk.itu.moapd.scootersharing.oska.databinding.ScooterListItemBinding
import dk.itu.moapd.scootersharing.oska.model.Scooter
import java.util.*

class RecyclerViewAdapter(private val scooterViewModel : ScooterViewModel) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    companion object {
        private val TAG = RecyclerViewAdapter::class.qualifiedName
    }


    init {
        scooterViewModel.scooters.observeForever { newData ->
            notifyDataSetChanged()
        }
    }
     class ViewHolder(private val binding: CardviewitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(scooter: Scooter) {

            val pictureRef = MainFragment.storageRef.child("images/testscooter.jpg")

            binding.cardScooterName.text = binding.root.context.getString(
                R.string.name, scooter._name
            )
            binding.cardScooterLocation.text = binding.root.context.getString(
                R.string.location, scooter._location
            )
            binding.cardScooterTimestamp.text = binding.root.context.getString(
                R.string.time, Date(scooter?._timestamp!!)
            )
            val picture = binding.scooterPicture
            pictureRef.downloadUrl.addOnSuccessListener {
                Glide.with(binding.root)
                    .load(it)
                    .into(picture)
                Log.w(ContentValues.TAG, "successfully loaded picture from storage bucket")


            }
            binding.root.setOnClickListener{
                MainFragment.selectedScooter =scooter

                Snackbar.make(
                    binding.root.rootView,
                    "${scooter._name} Selected!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "Creating a new ViewHolder")

        val inflater = LayoutInflater.from(parent.context)
        val binding = CardviewitemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getBindingAdapterPosition] which
     * will have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scooter = scooterViewModel.scooters.value?.get(position)
        Log.d(TAG, "Populate an item at position: $position")


        if (scooter != null) {
            holder.bind(scooter)
        }
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return scooterViewModel.scooters.value?.size ?: 0
    }


}

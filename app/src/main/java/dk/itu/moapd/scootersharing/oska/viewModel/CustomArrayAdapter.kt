package dk.itu.moapd.scootersharing.oska.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import dk.itu.moapd.scootersharing.oska.R
import dk.itu.moapd.scootersharing.oska.model.Scooter
import java.util.Date

/**
 * A customArrayAdapter for to showing scooters in a list
 */
class CustomArrayAdapter(context: Context, private var resource: Int, data: List<Scooter>) :
    ArrayAdapter<Scooter>(context, R.layout.scooter_list_item, data) {

    companion object {
        private val TAG = CustomArrayAdapter::class.qualifiedName
    }

    private class ViewHolder(view: View) {
        val name: TextView = view.findViewById(R.id.scooter_name_item)
        val location: TextView = view.findViewById(R.id.scooter_location_item)
        val timestamp: TextView = view.findViewById(R.id.scooter_time_item)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
        } else
            viewHolder = view.tag as ViewHolder

        val scooter = getItem(position)

        Log.d(TAG, "populate $position")

        viewHolder.name.text = parent.context.getString(R.string.name, scooter?._name)
        viewHolder.location.text =
            parent.context.getString(R.string.location, scooter?._translated_location)
        viewHolder.timestamp.text =
            parent.context.getString(R.string.time, Date(scooter?._timestamp!!))

        view?.tag = viewHolder
        return view!!
    }

}
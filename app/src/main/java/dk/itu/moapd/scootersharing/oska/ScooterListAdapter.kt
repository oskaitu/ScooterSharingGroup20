package dk.itu.moapd.scootersharing.oska

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.Date

class ScooterListAdapter(context: Context, private var resource: Int, data: List<Scooter>) :
    ArrayAdapter<Scooter>(context, R.layout.list_item_scooter, data) {

    companion object {
        private val TAG = ScooterListAdapter::class.qualifiedName
    }
    private class ViewHolder(view: View) {
        val name: TextView = view.findViewById(R.id.scooter_name)
        val location: TextView = view.findViewById(R.id.scooter_location)
        val timestamp: TextView = view.findViewById(R.id.scooter_time)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View{

        var view = convertView
        val viewHolder : ViewHolder

        if (view == null){
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(resource, parent,false)
            viewHolder = ViewHolder(view)
        } else
            viewHolder = view.tag as ViewHolder

        val scooter = getItem(position)

        Log.d(TAG, "populate $position")

        viewHolder.name.text = parent.context.getString(R.string.name, scooter?._name)
        viewHolder.location.text = parent.context.getString(R.string.location, scooter?._location)
        viewHolder.timestamp.text = parent.context.getString(R.string.time, Date(scooter?._timestamp!!))

        view?.tag = viewHolder
        return view!!
    }
}
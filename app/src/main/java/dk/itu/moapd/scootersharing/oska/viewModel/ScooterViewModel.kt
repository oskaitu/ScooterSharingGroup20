package dk.itu.moapd.scootersharing.oska.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.oska.model.Scooter
import dk.itu.moapd.scootersharing.oska.view.MainActivity

class ScooterViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _scooters = MutableLiveData<List<Scooter>>()
    val scooters : LiveData<List<Scooter>> = _scooters

    fun loadData()
    {
        db.collection("scooters")
            .addSnapshotListener{ value , error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }

                val scooterList = mutableListOf<Scooter>()
                for (doc in value!!){

                    scooterList.add(Scooter(
                        _name = doc.get("name") as String,
                        _location = doc.get("location") as String,
                        _timestamp = doc.get("timestamp") as Long)

                    )
                }
                _scooters.value = scooterList
            }
    }


}
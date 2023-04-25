package dk.itu.moapd.scootersharing.oska.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.oska.model.Receipt
import dk.itu.moapd.scootersharing.oska.model.Scooter
import dk.itu.moapd.scootersharing.oska.view.MainActivity
import dk.itu.moapd.scootersharing.oska.view.MainFragment

class ScooterViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val _scooters = MutableLiveData<List<Scooter>>()
    val scooters: LiveData<List<Scooter>> = _scooters


    /*
    Loader for making sure ScooterData is consistent across all users
     */
    fun loadData() {
        db.collection("scooters")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }

                val scooterList = mutableListOf<Scooter>()

                for (doc in value!!) {


                    scooterList.add(
                        Scooter(
                            _id = doc.id,
                            _name = doc.get("name") as String,
                            _location = doc.get("location") as String,
                            _timestamp = doc.get("timestamp") as Long,
                            _translated_location = doc.get("translated_location") as String?
                        )

                    )
                }
                _scooters.value = scooterList
            }
    }

    suspend fun loadTransactionData(userID : String) : List<Receipt> {
        val receipts = mutableListOf<Receipt>()
        db.collection("rental_history")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }

                val history = mutableListOf<String>()
                for (doc in value!!) {
                    if(doc.get("accountid") as String == userID)
                    {
                        history.add(

                            doc.get("ridesid") as String)
                    }
                }
                if(history.size != 0)
                {
                    db.collection("rides")
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error)
                                return@addSnapshotListener
                            }
                            while(history.isNotEmpty())
                            {
                                var check = history.removeLast()
                                for (doc in value!!) {
                                    if(doc.id == check)
                                    {
                                        //var scoot = scooters.value?.filter { s -> s._id==doc.get("scooterid")}
                                        receipts.add(
                                            Receipt(
                                                name = doc.id,
                                                startTime = doc.get("start_time") as Long,
                                                endTime = doc.get("end_time") as Long,
                                                startLocation = doc.get("start_location") as String,
                                                endLocation = doc.get("end_location") as String,
                                                distance = doc.get("distance") as Number,
                                                cost = doc.get("cost") as Number
                                            ))
                                    }
                                }
                            }
                            }
                }
    }
        return receipts
    }

    fun getScooter(id : String) : Scooter? {
        var scooter: Scooter? = null
        val docRef = db.collection("scooters").document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    scooter =
                        Scooter(
                            _id = id,
                            _name = document.get("name") as String,
                            _location = document.get("location") as String,
                            _timestamp = document.get("timestamp") as Long,
                            _translated_location = document.get("translated_location") as String?
                        )

                }
            }
        return scooter
    }

    /*
    Updater used for updating timestamps in update scooter
     */
    fun updateDocument(collection: String, item: String, fieldToUpdate: String, newData: Any) {
        // [START update_document]
        val ref = db.collection(collection).document(item)

        ref
            .update(fieldToUpdate, newData)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        // [END update_document]
    }

    /*
    This is for adding new scooters through an admin menu
     */
    fun addDocumentSimple(collection: String, data: HashMap<String, Any>, ) {
        // [START add_document]
        // Add a new document with a generated id.

        db.collection(collection)
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        // [END add_document]
    }

    /*
    specific function used by ActiveRideFragmentDialog to generate history
     */
    fun addDocumentRentalAndRides(
        rideData: HashMap<String, Any>,
        rentalData: HashMap<String, Any>
    ) {
        // [START add_document]
        // Add a new document with a generated id.

        db.collection("rides")
            .add(rideData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                rentalData["ridesid"] = documentReference.id
                rentalData["accountid"] = MainFragment.user?.email.toString()
                rentalData["costFK"] = rideData["cost"] as Any

                db.collection("rental_history")
                    .add(rentalData)
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            TAG,
                            "DocumentSnapshot written with ID: ${documentReference.id}"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error adding for rentals", e) }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document for rides", e)
                    }
                // [END add_document]
            }
    }

    fun deleteDocument(collection: String, id : String) {
        // [START delete_document]
        db.collection(collection).document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        // [END delete_document]
    }







}
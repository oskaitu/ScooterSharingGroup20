package dk.itu.moapd.scootersharing.oska.view

import android.content.ContentValues
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.oska.model.Receipt
import kotlinx.coroutines.awaitAll

class ReceiptFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val geocoder = (activity as MainActivity).geocoder
                ScooterList(geocoder)
            }
        }
    }
}

@Composable
fun ScooterList(geocoder: Geocoder) {
    val receipts = MainFragment.receipts
    //val receipts = transactions.map {
      //      t -> t.endLocation.split(",") }
    LazyColumn {
        items(receipts) { it ->
            ReceiptListItem(it)
        }
    }
}
@Composable
fun ReceiptListItem(receipt: Receipt) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = receipt.name,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Start Time: ${receipt.startTime}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "End Time: ${receipt.endTime}",
                    style = MaterialTheme.typography.body1
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Start Location: ${receipt.startLocation}",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "End Location: ${receipt.endLocation}",
                    style = MaterialTheme.typography.body2
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Distance: ${receipt.distance}",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "Cost: ${receipt.cost}",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

    fun convertCordsToAddress(geocoder: Geocoder, latitude: Double, longitude: Double) : String {

        val address: Address?
        var fulladdress = ""
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                address = addresses[0]
                fulladdress = address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
                var city = address.locality;
                var state = address.adminArea;
                var country = address.countryName;
                var postalCode = address.postalCode;
                var knownName = address.featureName; // Only if available else return NULL
            } else{
                fulladdress = "Location not found"
            }
        }
        return fulladdress
    }
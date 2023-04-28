package dk.itu.moapd.scootersharing.oska.view

import android.location.Address
import android.location.Geocoder
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import dk.itu.moapd.scootersharing.oska.R

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.scootersharing.oska.model.Receipt
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A special fragment written using jetpack compose, this contains a collection of receipts in a lazylist
 * that shows all history for a user.
 */
class ReceiptFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val geocoder = (activity as MainActivity).geocoder
                val nav = findNavController()
                val iconBack = Icons.Outlined.ArrowBack


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ScooterList(geocoder, nav, iconBack)
                } else {
                    ScooterListSimple(geocoder, nav, iconBack)
                }

            }

        }
        return view
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScooterList(geocoder: Geocoder, nav: NavController, iconBack: ImageVector) {
    val receipts = MainFragment.receipts
    val receiptsWithNoDuplicates = receipts.toSet().toList()

    LazyColumn {

        item {
            Button(
                onClick = { nav.navigate(R.id.fragment_main) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(100),
            ) {
                Image(
                    imageVector = iconBack,
                    contentDescription = "Pay with credit card"
                )
            }
        }
        items(receiptsWithNoDuplicates) { it ->
            val calc1 = it.startLocation.trim().split(",")
            val start = convertCordsToAddress(geocoder, calc1[0].toDouble(), calc1[1].toDouble())
            val calc2 = it.startLocation.trim().split(",")
            val end = convertCordsToAddress(geocoder, calc2[0].toDouble(), calc2[1].toDouble())

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault())

            val date = Instant.ofEpochMilli(it.endTime)
            val startTime = formatter.format(date)
            ReceiptListItem(it, start, end, startTime)
        }

    }
    //MainFragment.receipts.clear()
}

@Composable
fun ScooterListSimple(geocoder: Geocoder, nav: NavController, iconBack: ImageVector) {
    val receipts = MainFragment.receipts
    LazyColumn {
        item {
            Button(
                onClick = { nav.navigate(R.id.fragment_main) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40),


                ) {
                Image(
                    imageVector = iconBack,
                    contentDescription = "Pay with credit card"
                )
            }
        }
        items(receipts) { it ->
            val calc1 = it.startLocation.trim().split(",")
            val start = convertCordsToAddress(geocoder, calc1[0].toDouble(), calc1[1].toDouble())
            val calc2 = it.startLocation.trim().split(",")
            val end = convertCordsToAddress(geocoder, calc2[0].toDouble(), calc2[1].toDouble())
            ReceiptListItem(it, start, end, "")
        }
    }
}


@Composable
fun ReceiptListItem(receipt: Receipt, start: String, end: String, startTime: String) {


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
                text = receipt.scooterName,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = receipt.name,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = startTime,
                    style = MaterialTheme.typography.body1
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "From :  $start",
                    style = MaterialTheme.typography.body2
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "To : $end",
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
                    text = "Cost: ${receipt.cost}.00,-",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

fun convertCordsToAddress(geocoder: Geocoder, latitude: Double, longitude: Double): String {

    val address: Address?
    var fulladdress = ""
    val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

    if (addresses != null) {
        if (addresses.isNotEmpty()) {
            address = addresses[0]
            fulladdress =
                address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
            var city = address.locality;
            var state = address.adminArea;
            var country = address.countryName;
            var postalCode = address.postalCode;
            var knownName = address.featureName; // Only if available else return NULL
        } else {
            fulladdress = "Location not found"
        }
    }
    return fulladdress
}


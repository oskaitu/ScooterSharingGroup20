package dk.itu.moapd.scootersharing.oska.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.findNavController
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * A special fragment written with jetpack compose it contains the most recent receipt and payment thereof it is showed
 * directly after ending a ride
 */
class PaymentFragment : Fragment() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val nav = findNavController()
                val receipt = MainFragment.mostRecentRide

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val formatter =
                        DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
                    val date = Instant.ofEpochMilli(receipt.startTime)
                    val startTimeReal = formatter.format(date)
                    val date2 = Instant.ofEpochMilli(receipt.endTime)
                    val endTimeReal = formatter.format(date2)
                    ReceiptShower(
                        scooterName = receipt.scooterName,
                        ridePrice = receipt.cost,
                        distance = receipt.distance,
                        startTime = startTimeReal,
                        endTime = endTimeReal,
                        creditCardIcon = Icons.Filled.ShoppingCart,
                        nav = nav
                    )
                } else {
                    ReceiptShower(
                        scooterName = receipt.scooterName,
                        ridePrice = receipt.cost,
                        distance = receipt.distance,
                        startTime = "",
                        endTime = "",
                        creditCardIcon = Icons.Outlined.Check,
                        nav = nav
                    )
                }


            }
        }
    }
}

@Composable
fun ReceiptShower(
    scooterName: String,
    ridePrice: Number,
    distance: Number,
    startTime: String,
    endTime: String,
    creditCardIcon: ImageVector,
    nav: NavController

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Scooter name
        Text(
            text = scooterName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black
        )

        // Ride price
        Text(
            text = "Price: kr ${ridePrice},-",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Distance
        Text(
            text = "Distance: ${distance}m",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Start time
        Text(
            text = "Start time: $startTime",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        // End time
        Text(
            text = "End time: $endTime",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Pay with credit card button
        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Button(
                onClick = { nav.popBackStack() },
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    imageVector = creditCardIcon,
                    contentDescription = "Pay with credit card"
                )
            }

            Text(
                text = "Pay with credit card",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
package dk.itu.moapd.scootersharing.oska.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.fragment.app.Fragment
import dk.itu.moapd.scootersharing.oska.R

class ReceiptFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReceiptFeatureScreen()
            }
        }
    }
}

@Composable
fun ReceiptFeatureScreen() {
    Column(Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.title),
            style = MaterialTheme.typography.h1
        )
        Text(
            text = stringResource(R.string.subtitle),
            style = MaterialTheme.typography.h2
        )
        Text(
            text = stringResource(R.string.body),
            style = MaterialTheme.typography.body2
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { /* Handle click */ }, Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.confirm))
        }
    }
}

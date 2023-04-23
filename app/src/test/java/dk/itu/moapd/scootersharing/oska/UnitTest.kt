package dk.itu.moapd.scootersharing.oska

import android.content.Context
import android.location.Geocoder
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import dk.itu.moapd.scootersharing.oska.view.ActiveRideFragmentDialogue
import dk.itu.moapd.scootersharing.oska.view.MainActivity
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test


class UnitTest {



    @Test
    fun testConvertCordsToAddress() {
        val scenario = launchFragmentInContainer<ActiveRideFragmentDialogue>()
        scenario.onFragment { fragment ->
            val latitude = 37.7749
            val longitude = -122.4194

        val activity = fragment.requireActivity() as MainActivity
        activity.geocoder = Geocoder(activity.applicationContext)

        val result = fragment.convertCordsToAddress(latitude, longitude)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertNotEquals("Location not found", result)
    }
}
}

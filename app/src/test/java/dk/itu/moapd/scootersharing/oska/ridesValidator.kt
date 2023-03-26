package dk.itu.moapd.scootersharing.oska

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import net.bytebuddy.matcher.ElementMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MockedRidesValidator {

    lateinit var Database: RidesDB

    @Before
    fun setup() {
        val Database = mock(RidesDB::class.java)
    }

    /*
    @Test
    fun adding_to_db_works() {
        Mockito.`when`(Database.getRidesList()).thenReturn(true)
        assertThat(Database.getRidesList(), `is` (true))
    }
    /*
     */
/*

     */
Mockito.`when`(SUT.isConnectedToNetwork()).thenReturn(false)
        assertThat(SUT.isConnectedToNetwork(), `is` (false))
    }
 */


    }
package dk.itu.moapd.scootersharing.oska


import dk.itu.moapd.scootersharing.oska.view.getNameWithInitial

import org.junit.Assert.assertEquals

import org.junit.Test


class UnitTest {



    @Test
    fun testGetNameWithInitial() {
        val fullName = "John Doe"
        val nameWithInitial = getNameWithInitial(fullName)
        assertEquals("John D", nameWithInitial)
    }
}


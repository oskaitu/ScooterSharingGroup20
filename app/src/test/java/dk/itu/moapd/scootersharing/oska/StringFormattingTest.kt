package dk.itu.moapd.scootersharing.oska


import dk.itu.moapd.scootersharing.oska.view.getNameWithInitial

import org.junit.Assert.assertEquals

import org.junit.Test


class StringFormattingTest {



    @Test
    fun testGetNameWithInitial() {
        val fullName = "John Doe"
        val nameWithInitial = getNameWithInitial(fullName)
        assertEquals("John D", nameWithInitial)
    }

    @Test
    fun testGetNameWithInitialIfMiddleNamePresent() {
        val fullName = "John Fun Doe"
        val nameWithInitial = getNameWithInitial(fullName)
        assertEquals("John D", nameWithInitial)
    }
    @Test
    fun testGetNameWithInitialIfLongNamePresent() {
        val fullName = "John Fun Norris Doe"
        val nameWithInitial = getNameWithInitial(fullName)
        assertEquals("John D", nameWithInitial)
    }
    @Test
    fun testGetNameIfNoLastName() {
        val fullName = "John"
        val nameWithInitial = getNameWithInitial(fullName)
        assertEquals("John", nameWithInitial)
    }
}


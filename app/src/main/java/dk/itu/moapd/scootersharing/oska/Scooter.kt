package dk.itu.moapd.scootersharing.oska

import java.util.*

data class Scooter (val _name: String, var _location: String, var _timestamp: Long ){

    override fun toString(): String {
        var temp = Date(_timestamp)
        return "Scooter $_name is at $_location at $temp"
    }
}
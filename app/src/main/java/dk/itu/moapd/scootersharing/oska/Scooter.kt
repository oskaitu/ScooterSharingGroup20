package dk.itu.moapd.scootersharing.oska

import java.sql.Timestamp

data class Scooter(
    var _name: String,
    var _location: String,
    var _timestamp: Long
) {
    override fun toString(): String {
        return "[ Scooter ] $_name is placed at $_location ."
    }
}
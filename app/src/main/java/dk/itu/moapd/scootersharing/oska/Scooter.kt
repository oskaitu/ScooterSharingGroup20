package dk.itu.moapd.scootersharing.oska

data class Scooter (val _name: String, var _location: String, var _timestamp: Long ){


    override fun toString(): String {
        return "Scooter $_name is at $_location at $_timestamp"
    }



}
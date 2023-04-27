package dk.itu.moapd.scootersharing.oska.model

data class Receipt(
    val name: String,
    val startTime: Long,
    val endTime: Long,
    val startLocation: String,
    val endLocation: String,
    val distance: Number,
    val cost: Number,
    val scooterName: String,
)

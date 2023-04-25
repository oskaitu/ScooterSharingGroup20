package dk.itu.moapd.scootersharing.oska.model

data class Receipt(
    val name: String,
    val startTime: String,
    val endTime: String,
    val startLocation: String,
    val endLocation: String,
    val distance: String,
    val cost: String
)

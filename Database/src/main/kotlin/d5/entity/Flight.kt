package d5.entity

import java.time.LocalDateTime
import kotlinx.serialization.*

@Serializable
data class Flight(
    val flight_id: String,
    val flight_no: String,
    val scheduled_departure: String,
    val scheduled_arrival: String,
    val departure_airport: String,
    val arrival_airport: String,
    val status: String
)

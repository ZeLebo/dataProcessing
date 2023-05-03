package d5.dto

import kotlinx.serialization.Serializable

@Serializable
data class AirportTdo(val id: String, val flightDto: List<FlightDto>)

package d5.dto

import kotlinx.serialization.Serializable

@Serializable
data class FlightDto(val id: String, val airport: String, val airportDest: String)
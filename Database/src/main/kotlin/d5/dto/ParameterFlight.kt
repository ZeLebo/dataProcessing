package d5.dto

import kotlinx.serialization.Serializable

@Serializable
data class ParameterFlight(val flights: List<UnitFlight>)

@Serializable
data class UnitFlight(val flight_no: String, val departure_airport: String, val arrival_airport: String)

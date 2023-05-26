package d5.dto

import kotlinx.serialization.Serializable

@Serializable
data class Seat(val number: String, val boarding_no: Int)
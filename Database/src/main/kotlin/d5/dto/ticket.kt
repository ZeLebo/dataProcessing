package d5.dto

import kotlinx.serialization.Serializable

@Serializable
data class ticket(val ticket_no: String, val price: Double)

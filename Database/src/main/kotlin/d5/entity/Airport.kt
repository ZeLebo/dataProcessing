package d5.entity

import kotlinx.serialization.Serializable

@Serializable
data class Airport(val id: String, val name: String, val city: String)

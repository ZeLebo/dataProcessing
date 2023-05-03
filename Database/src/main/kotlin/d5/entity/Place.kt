package d5.entity

import kotlinx.serialization.Serializable

enum class Type {
    Business, Economy
}

@Serializable
class Place(
val number: String,
val flight: Flight,
) {
    val type : Type = Type.Economy
    var checked: Boolean = false
    lateinit var checkedBy: String
    var bought = false
    var available: Boolean = true
}
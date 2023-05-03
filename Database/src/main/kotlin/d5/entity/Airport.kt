package d5.entity

class Airport(
    val id: String
) {
    lateinit var city: String
    var schedule: MutableSet<Flight> = mutableSetOf()
}
package d5.entity

import java.time.LocalDateTime
import kotlinx.serialization.*

@Serializable
class Flight(
    val id: String,
) {
    @Transient
    var timeDest: LocalDateTime = LocalDateTime.now()
    
    @Transient
    var timeSource: LocalDateTime = LocalDateTime.now().plusDays(2)
    @Transient
    lateinit var airportDest: Airport
    @Transient
    lateinit var airportSource: Airport
    var places: MutableSet<Place> = mutableSetOf()
    val priceBusiness = 1000
    val priceEconomy = 500
}
package d5.service

import d5.repository.Temp

class Usercase(
    private val database: Temp
) {
    fun getCities() = database.getCities()
    fun getAirports() = database.getAirports()
    fun getAirports(city: String) = database.getAirports(city)
    fun getInboundSchedule(airport: String) = database.getInboundSchedule(airport)
    fun getOutboundSchedule(airport: String) = database.getOutboundSchedule(airport)
    fun getFlightsByAirports(source: String, dest: String) = database.getFlightsByAirports(source, dest)
    fun getFlightsByCities(source: String, dest: String) = database.getFlightsByCities(source, dest)
    fun getFlightsByAirportAndCity(source: String, dest: String) = database.getFlightsByAirportAndCity(source, dest)
    fun getFlightsByCityAndAirport(source: String, dest: String) = database.getFlightsByCityAndAirport(source, dest)

    fun bookTheFlight(flightId: String, place: String, name: String) {
        //check existence of flight
        val flight = database.getFlightById(flightId) ?: throw Exception("No such flight")
        // check available places
        val seat = flight.places.find { it.number == place } ?: throw Exception("No such seat for flight")
        if (!seat.available) {
            throw Exception("Seat is not available")
        } else {
            seat.available = false
            seat.checkedBy = name
        }
    }

    fun checkoutTheFlight(flightId: String, place: String, name: String) {
        val flight = database.getFlightById(flightId) ?: throw Exception("No such flight")
        val seat = flight.places.find { it.number == place } ?: throw Exception("No such seat for flight")
        if (!seat.bought && seat.checkedBy == name) {
            // buying the seat
            seat.bought = true
        } else {
            throw Exception("You can't buy this place")
        }
    }
}
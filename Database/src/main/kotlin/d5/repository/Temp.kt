package d5.repository

import d5.entity.*

class Temp {
    private var airports: MutableSet<Airport> = mutableSetOf()
    private var flights: MutableSet<Flight> = mutableSetOf()
    var places: MutableSet<Place> = mutableSetOf()

    init {
        var flightOne = Flight("first-flight")
        var flightTwo = Flight("second-flight")
        var airportOne = Airport("first-airport")
        airportOne.city = "first-city"
        var airportTwo = Airport("second-airport")
        airportTwo.city = "second-city"

        airportOne.schedule.add(flightOne)
        airportTwo.schedule.add(flightTwo)

        flightOne.airportDest = airportOne
        flightOne.airportSource = airportTwo
        flightTwo.airportSource = airportOne
        flightTwo.airportDest = airportTwo

        flightOne.places.add(Place("firstPlace", flightOne))

        airports.addAll(listOf(airportOne, airportTwo))
        flights.addAll(listOf(flightTwo, flightOne))
    }

    fun getCities(): MutableSet<String> {
        val result = mutableSetOf<String>()
        for (airport in airports) {
            result.add(airport.city)
        }
        return result
    }

    fun getAirports(): MutableSet<Airport> {
        return airports
    }

    fun getAirports(city: String): MutableSet<Airport> {
        return airports.filter { it.city == city }.toMutableSet()
    }

    fun getInboundSchedule(airport: String): MutableSet<Flight> {
        return flights.filter { it.airportDest.id == airport }.toMutableSet()
    }

    fun getOutboundSchedule(airport: String): MutableSet<Flight> {
        return flights.filter { it.airportSource.id == airport }.toMutableSet()
    }

    fun getFlightsByAirports(source: String, dest: String): MutableSet<Flight> {
        return flights.filter { it.airportDest.id == dest }.filter { it.airportSource.id == source }.toMutableSet()
    }

    fun getFlightsByCities(source: String, dest: String): MutableSet<Flight> {
        return flights.filter { it.airportDest.city == dest }.filter { it.airportSource.city == dest }.toMutableSet()
    }

    fun getFlightsByAirportAndCity(source: String, dest: String): MutableSet<Flight> {
        return flights.filter { it.airportSource.id == source }.filter { it.airportDest.city == dest }.toMutableSet()
    }

    fun getFlightsByCityAndAirport(source: String, dest: String): MutableSet<Flight> {
        return flights.filter { it.airportSource.city == source}.filter { it.airportDest.id == dest }.toMutableSet()
    }

    fun getFlightById(flightId: String): Flight? {
        return flights.find {it.id == flightId}
    }
}
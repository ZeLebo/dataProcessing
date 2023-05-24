package d5.service

import d5.dto.ParameterFlight
import d5.dto.ticket
import d5.entity.Airport
import d5.repository.Postgres
import sun.security.ec.point.ProjectivePoint.Mutable

class Usercase(
    private val database: Postgres
) {
    fun addAirport(code: String) = database.addAirport(code)
    fun getCities() = database.getAllCities()
    fun getAirports() = database.getAllAirports()
    fun getAirports(city: String) = database.getAllAirports(city)
    /*
    fun getAirports(city:String): MutableSet<Airport> {
        return if (city == null) {
            database.getAllAirports()
        } else {
            database.getAllAirports(city)
        }
    }
     */
    fun getInboundSchedule(airport: String) = database.getInboundSchedule(airport)
    fun getOutboundSchedule(airport: String) = database.getOutboundSchedule(airport)
    /*
    fun getFlightsByCities(source: String, dest: String) = database.getFlightsByCities(source, dest)
    fun getFlightsByAirportAndCity(source: String, dest: String) = database.getFlightsByAirportAndCity(source, dest)
    fun getFlightsByCityAndAirport(source: String, dest: String) = database.getFlightsByCityAndAirport(source, dest)

    */
    fun getFlightsByAirport(source: String, dest: String, n: Int = 1) = database.getFlightsByAirports(source, dest, n)
    fun getFlightsByAirportAndCity(source: String, dest: String, n: Int = 1): List<ParameterFlight> {
        val airports = database.getAllAirports(dest)
        val res : MutableList<ParameterFlight> = mutableListOf()
        airports.map {
            res.addAll(database.getFlightsByAirports(source, it.id, n))
        }
        return res
    }

    fun getFlightsByCityAndAirport(source: String, dest: String, n: Int = 1) : List<ParameterFlight> {
        val airports = database.getAllAirports(source)
        val res : MutableList<ParameterFlight> = mutableListOf()
        airports.map {
            res.addAll(database.getFlightsByAirports(it.id, dest, n))
        }
        return res
    }
    fun bookTheFlight(name: String, identification: String, flight_id: Int, fare_conditions: String): ticket = database.bookTheFlight(name, identification, flight_id, fare_conditions)

    fun checkoutTheFlight(ticket_no: String) = database.checkoutFlight(ticket_no)
}
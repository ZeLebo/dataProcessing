package d5.controller.web

import d5.repository.Temp
import d5.service.Usercase
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import d5.dto.*
import d5.entity.Flight
import io.ktor.http.*
import io.ktor.server.plugins.swagger.*

fun Application.configureRouting() {
    val service = Usercase(Temp())

    routing {
        get("/api/v1/cities") {
            val result = service.getCities().map {
                CityTdo(it)
            }
            if (result.isEmpty()) {
                call.respondText("No cities found")
            } else {
                call.respond(Json.encodeToString(result))
            }
        }
        get("/api/v1/airports") {
            val city = call.parameters["city"]
            val result = mutableListOf<AirportTdo>()
            val airports = if (city != null) service.getAirports(city) else service.getAirports()
            airports.map { airport ->
                val flights: MutableList<FlightDto> = mutableListOf()
                airport.schedule.map {
                    flights.add(FlightDto(it.id, it.airportSource.id, it.airportDest.id))
                }
                result.add(AirportTdo(airport.id, flights))
            }
            if (result.isEmpty()) {
                call.respondText("No airports found")
            } else {
                call.respond(Json.encodeToString(result))
            }
        }
        // inbound and outbound schedule for airports
        get("/api/v1/airports/inbound") {
            val airport = call.parameters["airport"]
            if (airport == null) {
                call.respondRedirect("/api/v1/airports")
                return@get
            }
            val result: MutableList<FlightDto> = mutableListOf()
            service.getInboundSchedule(airport).map { flight ->
                result.add(FlightDto(flight.id, flight.airportSource.id, flight.airportDest.id))
            }
            if (result.isEmpty()) {
                call.respond(HttpStatusCode.BadGateway, "No flights for this airport")
            } else {
                call.respond(Json.encodeToString(result))
            }
        }
        get("/api/v1/airports/outbound") {
            val airport = call.parameters["airport"]
            if (airport == null) {
                call.respondRedirect("api/v1/airports")
                return@get
            }
            val result: MutableList<FlightDto> = mutableListOf()
            service.getOutboundSchedule(airport).map { flight ->
                result.add(FlightDto(flight.id, flight.airportSource.id, flight.airportDest.id))
            }
            if (result.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "No flights for this airport")
            } else {
                call.respond(Json.encodeToString(result))
            }
        }
        get("api/v1/flights") {
            val airportSource = call.parameters["airportSource"]
            val airportDest = call.parameters["airportDest"]
            val citySource = call.parameters["citySource"]
            val cityDest = call.parameters["cityDest"]

            val result: MutableList<FlightDto> = mutableListOf()
            val fromDb: MutableSet<Flight> = mutableSetOf()
            if (airportSource != null && airportDest != null) {
                fromDb.addAll(service.getFlightsByAirports(airportSource, airportDest))
            } else if (citySource != null && cityDest != null) {
                fromDb.addAll(service.getFlightsByCities(citySource, cityDest))
            } else if (airportSource != null && cityDest != null) {
                fromDb.addAll(service.getFlightsByAirportAndCity(airportSource, cityDest))
            } else if (citySource != null && airportDest != null) {
                fromDb.addAll(service.getFlightsByCityAndAirport(citySource, airportDest))
            }

            fromDb.map { flight ->
                result.add(FlightDto(flight.id, flight.airportSource.id, flight.airportDest.id))
            }

            if (result.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "There's no flight according to these parameters")
            } else {
                call.respond(Json.encodeToString(result))
            }
        }
        post("/api/v1/flights/booking") {
            val flightId = call.parameters["flightId"]
            val seatId = call.parameters["seat"]
            val name = call.parameters["name"]

            if (listOf(flightId, seatId, name).any { it.isNullOrEmpty() }) {
                call.respond(HttpStatusCode.BadRequest, "Parameters are not fully stated")
                return@post
            }

            try {
                service.bookTheFlight(flightId!!, seatId!!, name!!)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "You cannot book this place")
            } finally {
                call.respond("You've booked the seat to the flight")
            }
        }
        post("/api/v1/flights/checkout") {
            val flightId = call.parameters["flightId"]
            val seatId = call.parameters["seat"]
            val name = call.parameters["name"]

            if (listOf(flightId, seatId, name).any { it.isNullOrEmpty() }) {
                call.respondText("Parameters are not fully stated")
                return@post
            }
            try {
                service.checkoutTheFlight(flightId!!, seatId!!, name!!)
            } catch (e: Exception) {
                call.respond(400)
                call.respondText(e.toString())
            } finally {
                call.respond("You've checkout out to the seat on the flight")
            }

        }
        swaggerUI(path = "openapi", swaggerFile = "openapi/documentation.json")
    }
}
package d5.controller.web

import d5.service.Usercase
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import d5.dto.*
import d5.entity.Airport
import d5.repository.Postgres
import io.ktor.http.*
import io.ktor.server.plugins.swagger.*

fun Application.configureRouting() {
    val service = Usercase(Postgres())

    routing {
        get("/api/v1/cities") {
            val result = service.getCities().map {
                CityTdo(it)
            }
            if (result.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "No cities found")
            } else {
                call.respond(Json.encodeToString(result))
            }
        }
        get("/api/v1/airports") {
            val city = call.parameters["city"]
            val result = mutableSetOf<Airport>()
            if (city != null) {
                result.addAll(service.getAirports(city))
            } else {
                result.addAll(service.getAirports())
            }
            if (result.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "No airports found")
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
            val result = service.getInboundSchedule(airport)
            if (result.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "No flights for this airport")
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
            val result = service.getOutboundSchedule(airport)
            if (result.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "No flights for this airport")
            } else {
                call.respond(Json.encodeToString(result))
            }
        }

        get("api/v1/flights") {
            val airportSource = call.parameters["airportSource"]
            val airportDest = call.parameters["airportDest"]
//            val citySource = call.parameters["citySource"]
//            val cityDest = call.parameters["cityDest"]
            val upperBound = call.parameters["bound"]

            try {
                upperBound?.toInt()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Amount of peresadky cannot be not int")
                return@get
            }

            val result = service.getFlightsByAirport(airportSource!!, airportDest!!, upperBound?.toInt() ?: 1)

            if (result.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "There's no flight according to these parameters")
            } else {
                call.respond(Json.encodeToString(result))
            }
        }
        post("/api/v1/flights/booking") {
            val flightId = call.parameters["flightId"]
            val identification = call.parameters["identification"] ?: "123456789"
            val name = call.parameters["name"]
            val fare_conditions = call.parameters["fare_conditions"] ?: "Economy"

            if (listOf(flightId, identification, name, fare_conditions).any { it.isNullOrEmpty() }) {
                call.respond(HttpStatusCode.BadRequest, "Parameters are not fully stated")
                return@post
            }

            if (fare_conditions !in listOf("Economy", "Business", "Comfort")) {
                call.respond(HttpStatusCode.BadRequest, "Wrong fare conditions")
                return@post
            }

            var res: ticket = ticket("", 0.0)

            try {
                res = service.bookTheFlight(name!!, identification, flightId!!.toInt(), fare_conditions)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "You cannot book this place due to $e")
            } finally {
                if (res.ticket_no.isNotEmpty()) {
                    call.respond(Json.encodeToString(res))
                } else {
                    call.respond(HttpStatusCode(400, "No place for this flight left available"))
                }
            }
        }
        post("/api/v1/flights/checkout") {
            val ticket_no = call.parameters["ticket_no"]
            var seat = Seat("",0)

            if (ticket_no.isNullOrEmpty()) {
                call.respondText("Specify the ticket_no")
                return@post
            }
            try {
                seat = service.checkoutTheFlight(ticket_no)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "You cannot checkout this place due to ${e.localizedMessage}")
            } finally {
                call.respond(Json.encodeToString(seat))
            }

        }
        swaggerUI(path = "openapi", swaggerFile = "openapi/documentation.json")
    }
}
package d5.controller.web

import d5.repository.Temp
import d5.service.Usercase
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.configureRouting() {
    val service = Usercase(Temp())

    routing {
        get("/api/v1/cities") {
            @Serializable
            data class City(val city: String)
            val result = service.getCities().map {
                City(it)
            }
            if (result.isEmpty()) {
                call.respondText("No cities found")
            } else {
                call.respond(Json.encodeToString(result))
            }
        }
    }
}
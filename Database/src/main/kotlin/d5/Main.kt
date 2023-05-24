package d5

import d5.controller.web.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun test() {
    // TODO: Unit-tests 
    // TODO: How optimizer in postgres works 
    // TODO: change getFlightsAirports to recursive 
}

fun Application.module() {
    configureRouting()
}

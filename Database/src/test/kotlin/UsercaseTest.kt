import d5.repository.Postgres
import d5.service.Usercase
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.assertNotEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsercaseTest {
    var database: Postgres = Postgres()
    val usercase = Usercase(database)

    @Test
    fun getAllAirports() {
        val airports = usercase.getAirports()
        assertEquals(104, airports.size)
    }

    @Test
    fun increaseAndTestGetAirports() {
        usercase.addAirport("AAA")
        var airports = usercase.getAirports()
        assertEquals(105, airports.size)
        database.deleteAirport("AAA")
        airports = usercase.getAirports()
        assertEquals(104, airports.size)
    }

    @Test
    fun `get all airports for city true`() {
        val airports = usercase.getAirports("Москва")
        assertEquals(3, airports.size)
    }

    @Test
    fun `get all airports for city false`() {
        val airports = usercase.getAirports("NOT_CITY")
        assertEquals(0, airports.size)
    }

    @Test
    fun getAllCities() {
        val cities = usercase.getCities()
        assertEquals(101, cities.size)
    }

    @Test
    fun getAllCitiesFalse() {
        usercase.addAirport("AAA")
        val cities = usercase.getCities()
        assertNotEquals(101, cities.size)
        database.deleteAirport("AAA")
    }


    @Test
    fun `check path between 2 airports exist`() {
        val result = usercase.getFlightsByAirport("UIK", "DME", 3)
        assertEquals(32, result.size)
    }

    @Test
    fun `check path between 2 airports not exist`() {
        val result = usercase.getFlightsByAirport("UIK", "DME", 1)
        assertEquals(0, result.size)
    }

    @Test
    fun `test booking for flight`() {
        val ticket = usercase.bookTheFlight("IVANOV", "test", 12, "Economy")
        assertNotEquals(ticket.ticket_no, "")
    }

    @Test
    fun `test booking for flight not exist`() {
        Assertions.assertThrows(Exception::class.java) {
            usercase.bookTheFlight("Ivanov", "test", -1, "Economy")
        }
    }

    @Test
    fun `test checkout for flight`() {
        val ticket = usercase.bookTheFlight("IVANOV", "test", 12, "Economy")
        assertNotEquals(ticket.ticket_no, "")
        val result = usercase.checkoutTheFlight(ticket.ticket_no)
        assertNotEquals(result.number, "")
    }

    @Test
    fun `test checkout for flight second time`() {
        val ticket = usercase.bookTheFlight("IVANOV", "test", 12, "Economy")
        assertNotEquals(ticket.ticket_no, "")
        var result = usercase.checkoutTheFlight(ticket.ticket_no)
        assertNotEquals(result.number, "")
        Assertions.assertThrows(Exception::class.java) {
            usercase.checkoutTheFlight(ticket.ticket_no)
        }
    }
}
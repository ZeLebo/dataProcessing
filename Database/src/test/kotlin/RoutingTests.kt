import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Test
import top.jfunc.json.impl.JSONObject
import kotlin.test.assertEquals

class RoutingTests {
    private fun makeRequest(url: String): Response {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        return client.newCall(request).execute()
    }

    @Test
    fun `simple not valid request`() {
        val response = makeRequest("http://localhost:8080/api/v1/ciiii")
        assertEquals(404, response.code)
    }

    @Test
    fun `get all cities 200`() {
        val response = makeRequest("http://localhost:8080/api/v1/cities")
        assertEquals(200, response.code)
    }

    @Test
    fun `get all airports`() {
        val response = makeRequest("http://localhost:8080/api/v1/airports")
        assertEquals(200, response.code)
    }

    @Test
    fun `get all airports and count`() {
        val response = makeRequest("http://localhost:8080/api/v1/airports")
        assertEquals(200, response.code)

        val airports = response.body?.string()?.split("id").orEmpty().size - 1
        assertEquals(104, airports)

    }

    @Test
    fun `get all airports from city`() {
        val response = makeRequest("http://localhost:8080/api/v1/airports?city=Москва")
        assertEquals(200, response.code)
    }

    @Test
    fun `get 400 error for unknown city`() {
        val response = makeRequest("http://localhost:8080/api/v1/airports?city=NOT_CITY")
        assertEquals(400, response.code)
    }

    @Test
    fun `get inbound schedule for airport return 200 and correct size`() {
        val response = makeRequest("http://localhost:8080/api/v1/airports/inbound?airport=DME")
        assertEquals(200, response.code)
        assertEquals(1601, response.body?.string()?.split("id").orEmpty().size - 1)
    }

    @Test
    fun `get inbound schedule for unknown airport returns 400`() {
        val response = makeRequest("http://localhost:8080/api/v1/airports/inbound?airport=DMEasdf")
        assertEquals(400, response.code)
    }

    @Test
    fun `get outbound schedule for airport return 200 and correct size`() {
        val response = makeRequest("http://localhost:8080/api/v1/airports/outbound?airport=DME")
        assertEquals(200, response.code)
        assertEquals(1599, response.body?.string()?.split("id").orEmpty().size - 1)
    }

    @Test
    fun `get outbound schedule for unknown airport return 400`() {
        val response = makeRequest("http://localhost:8080/api/v1/airports/outbound?airport=DMEasdf")
        assertEquals(400, response.code)
    }

    @Test
    fun `get list of routes between airports`() {
        val response = makeRequest("http://localhost:8080/api/v1/flights?airportSource=UIK&airportDest=DME&bound=3")
        assertEquals(200, response.code)
        assertEquals(32, response.body?.string()?.split("flights").orEmpty().size - 1)
    }

    @Test
    fun `get list of routes between unknown airports return 400`() {
        val response = makeRequest("http://localhost:8080/api/v1/flights?airportSource=UIKasdf&airportDest=DMEasdf&bound=3")
        assertEquals(400, response.code)
    }

}

fun main() {
    val client = OkHttpClient()
    val body = FormBody.Builder().build()
    val request = Request.Builder()
        .url("http://localhost:8080/api/v1/flights/booking?flightId=10&name=zhora&fare_condition=Economy")
        .post(body)
        .build()
    val response = client.newCall(request).execute()

    // convert response.body to string
    val jsonString = response.body?.string().orEmpty()

    // I have this response {"ticket_no":"9751549115d14","price":0.0} how to extract ticket_no without GSON
    // JSONObject jsonObject = new JSONObject(jsonString);
    //String ticketNo = jsonObject.getString("ticket_no");
    //System.out.println(ticketNo);
    val jsonObject = JSONObject(jsonString)
    println(jsonObject["ticket_no"])
}

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import top.jfunc.json.impl.JSONObject
import kotlin.test.assertEquals

class BookingTest {
    @Test
    fun `book the flight return 200 for good conditions`() {
        val client = OkHttpClient()
        val body = FormBody.Builder().build()
        val request = Request.Builder()
            .url("http://localhost:8080/api/v1/flights/booking?flightId=10&name=zhora&fare_condition=Economy")
            .post(body)
            .build()
        val response = client.newCall(request).execute()
        assertEquals(200, response.code)
    }

    @Test
    fun `book the flight on arrived flight return 400`() {
        val client = OkHttpClient()
        val body = FormBody.Builder().build()
        val request = Request.Builder()
            .url("http://localhost:8080/api/v1/flights/booking?flightId=1&name=zhora&fare_condition=Economy")
            .post(body)
            .build()
        val response = client.newCall(request).execute()
        assertEquals(400, response.code)
        assertTrue(response.body?.string().orEmpty().contains("Flight already arrived"))
    }

    @Test
    fun `checkout the ticket return 200`() {
        val client = OkHttpClient()
        val body = FormBody.Builder().build()
        val request = Request.Builder()
            .url("http://localhost:8080/api/v1/flights/booking?flightId=10&name=zhora&fare_condition=Economy")
            .post(body)
            .build()
        val response = client.newCall(request).execute()
        assertEquals(200, response.code)
        val jsonString = response.body?.string().orEmpty()
        assertTrue(jsonString.contains("ticket_no"))

        val jsonObject = JSONObject(jsonString)
        val ticket_no = jsonObject["ticket_no"].toString()


        val request2 = Request.Builder()
            .url("http://localhost:8080/api/v1/flights/checkout?ticket_no=${ticket_no}")
            .post(body)
            .build()
        val response2 = client.newCall(request2).execute()
        assertEquals(200, response2.code)
    }

    @Test
    fun `checkout for wrong ticket return 400`() {
        val ticket_no = "AAAAA"
        val client = OkHttpClient()
        val body = FormBody.Builder().build()
        val request = Request.Builder()
            .url("http://localhost:8080/api/v1/flights/checkout?ticket_no=$ticket_no")
            .post(body)
            .build()
        val response = client.newCall(request).execute()
        assertEquals(400, response.code)
    }
}
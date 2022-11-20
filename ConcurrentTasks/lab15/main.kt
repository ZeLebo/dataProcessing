package redirect

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/*
* Simple HTTP server that redirects all requests to a given URL.
* Using threads to handle multiple requests.
* Before start make sure that something is listening on the port.
* */
fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) args[0].toInt() else 9090
    val ip = if (args.size > 1) args[1] else "localhost"
    val ipPort = if (args.size > 2) args[2].toInt() else 80

    ServerSocket(port).use { server ->
        while (true) {
            val socket = server.accept()
            Thread {
                try {
                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                    val writer = PrintWriter(socket.getOutputStream())
                    val request = reader.readLine()
                    val uri = request.split(" ")[1]
                    val response = HttpClient.newHttpClient().send(HttpRequest.newBuilder()
                        .uri(URI.create("http://$ip:$ipPort$uri")).build(),
                            HttpResponse.BodyHandlers.ofString())
                    writer.println("HTTP/1.1 200 OK")
                    writer.println("Content-Type: text/html")
                    writer.println("Content-Length: ${response.body().length}")
                    writer.println()
                    writer.println(response.body())
                    writer.flush()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    socket.close()
                }
            }.start()
        }
    }
}
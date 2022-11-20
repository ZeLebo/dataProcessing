package server

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import java.net.URL
import java.util.concurrent.LinkedBlockingQueue
fun main(args: Array<String>) {
    val address = if (args.isNotEmpty()) args[0] else "https://en.wikipedia.org/wiki/Spider"
    val url = URL(address)
    val responseLines = LinkedBlockingQueue<String>()

    Thread {
        val socket = Socket(url.host, if (url.port != -1) url.port else 80)
        val request = "GET ${url.path} HTTP/1.1\nHost: ${url.host}\nAccept: json/text/*\nConnection: close\n\n"

        print("Request: $request\nResponse:\n")
        socket.getOutputStream().write(request.toByteArray())

        // start to read the response
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        reader.lines().forEach {
            responseLines.add(it) }
    }.start()

    while (true) {
        for (i in 0 until 200) {
            val line = responseLines.take() ?: return
            if (line.isEmpty()) { return }
            println(line)
        }
        println("Press ENTER to see more lines:")
        readln()
    }
}
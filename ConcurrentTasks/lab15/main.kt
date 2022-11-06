package redirect

import com.sun.security.ntlm.Server
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Input: <port> <ip> <ip_port>")
        return
    }
    val port = if (args.isNotEmpty()) args[0].toInt() else 8080
    val ip = args[1]
    val ipPort = if (args.size > 2) args[2].toInt() else 8000
    // get all the connections on port and redirect them to ip:ipPort
    // every connection is handled in a separate thread
    Server(port, ip, ipPort).start()
}

class Server(private val port: Int, private val ip: String, private val ipPort: Int) : Thread() {
    override fun run() {
        val serverSocket = java.net.ServerSocket(port)
        while (true) {
            val socket = serverSocket.accept()
            Thread(Redirect(socket, ip, ipPort)).start()
        }
    }

    class Redirect(private val socket: Socket, private val ip: String, private val ipPort: Int) : Runnable {
        override fun run() {
            try {
                val out = PrintWriter(socket.getOutputStream(), true)
                val `in` = BufferedReader(InputStreamReader(socket.getInputStream()))
                val socket2 = Socket(ip, ipPort)
                val out2 = PrintWriter(socket2.getOutputStream(), true)
                val in2 = BufferedReader(InputStreamReader(socket2.getInputStream()))
                Thread(Redirect2(socket, in2, out2)).start()
                var inputLine: String?
                while (`in`.readLine().also { inputLine = it } != null) {
                    out2.println(inputLine)
                }
                socket2.close()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class Redirect2(private val socket: Socket, private val `in`: BufferedReader, private val out: PrintWriter) : Runnable {
        override fun run() {
            try {
                var inputLine: String?
                while (`in`.readLine().also { inputLine = it } != null) {
                    out.println(inputLine)
                }
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

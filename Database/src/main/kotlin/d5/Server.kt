package d5

import com.sun.net.httpserver.HttpServer
import java.io.OutputStream
import java.net.InetSocketAddress
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class Server (
  private val threadPool: ThreadPoolExecutor = Executors.newFixedThreadPool(10) as ThreadPoolExecutor
) {
  private fun getParams(paramString: String): Map<String,String> {
    val params = mutableMapOf<String,String>()

    paramString
      .substring(paramString.indexOf("?")+1, paramString.length)
      .split(Regex("&"))
      .forEach {
        params[it.split("=")[0]] = it.split("=")[1]
      }

    return params.toMap()
  }

  fun simpleServer(port: Int) {
    HttpServer.create(InetSocketAddress(port), 0).apply {
      executor = threadPool
      println("Server runs at: 127.0.0.1:${address.port}")

      createContext("/api") { http ->
        when (http.requestMethod) {
          "GET" -> {
            http.responseHeaders.add("Content-type", "text/plain")
            http.sendResponseHeaders(200, 0)
            val os: OutputStream = http.responseBody
            if (http.requestURI.toString().indexOf("?") > 0) {
              os.write(getParams(http.requestURI.toString()).toString().encodeToByteArray())
              os.flush()
            } else {
              os.write("Hello from server!".encodeToByteArray())
              os.flush()
            }
            http.close()
          }
          "POST" -> {
            http.sendResponseHeaders(405, 0)
          }
        }
      }
      createContext("/api/hello") { http ->
        when (http.requestMethod) {
          "GET" -> {
            http.responseHeaders.add("Content-type", "text/plain")
            http.sendResponseHeaders(200, 0)
            val os: OutputStream = http.responseBody
            os.write("Hello world!".encodeToByteArray())
            if (http.requestURI.toString().indexOf("?") > 0) {
              // we have params
              os.write(getParams(http.requestURI.toString()).toString().encodeToByteArray())
            }
            os.flush()
            http.close()
          }
          else -> {
            http.sendResponseHeaders(404, 0)
          }
        }
      }
      start()
    }
  }
}

package selectserver

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) args[0].toInt() else 9090
    val ip = if (args.size > 1) args[1] else "localhost"
    val ipPort = if (args.size > 2) args[2].toInt() else 80

    println("Starting server on localhost:$port, redirecting to $ip:$ipPort")

    val selector = Selector.open()
    val serverSocketChanel = ServerSocketChannel.open()
    serverSocketChanel.bind(InetSocketAddress("localhost", port))
        .configureBlocking(false)
        .register(selector, SelectionKey.OP_ACCEPT)

    while (true) {
        selector.select()
        val keys = selector.selectedKeys()
        val iterator = keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            if (key.isAcceptable) {
                val server = key.channel() as ServerSocketChannel
                val client = server.accept()
                client.configureBlocking(false)
                client.register(selector, SelectionKey.OP_READ)
                println("Accepted connection from ${client.socket().remoteSocketAddress}")
            }
            if (key.isReadable) {
                val client = key.channel() as SocketChannel
                val buffer = ByteBuffer.allocate(1024)
                val read = client.read(buffer)
                if (read == -1) {
                    client.close()
                } else {
                    buffer.flip()

                    val redirectSocketChannel = SocketChannel.open()
                    try {
                        redirectSocketChannel.connect(InetSocketAddress(ip, ipPort))
                    } catch (e: Exception) {
                        client.write(ByteBuffer.wrap("HTTP/1.1 502 Bad Gateway\r\n\r\n".toByteArray()))
                        client.write(ByteBuffer.wrap("Cannot connect to the server".toByteArray()))
                        client.close()
                        return
                    }
                    redirectSocketChannel.write(buffer)

                    val response = ByteBuffer.allocate(1024)
                    val tmp = redirectSocketChannel.read(response)

                    if (tmp == -1) {
                        redirectSocketChannel.close()
                    } else {
                        response.flip()
                        client.write(response)
                    }
                }
            }
            iterator.remove()
        }
    }
}

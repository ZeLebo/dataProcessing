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
    val clientChannel = ServerSocketChannel.open()

    try {
        clientChannel.bind(InetSocketAddress("localhost", port))
            .configureBlocking(false)
            .register(selector, SelectionKey.OP_ACCEPT)
    } catch (e: Exception) {
        println("Error starting server: ${e.message}")
        return
    }

    while (true) {
        selector.select()
        val clientKeys = selector.selectedKeys()
        val clientIterator = clientKeys.iterator()

        while (clientIterator.hasNext()) {
            val key = clientIterator.next()
            clientIterator.remove()

            if (key.isAcceptable) {
                val client = clientChannel.accept()
                client.configureBlocking(false)
                val clientKey = client.register(selector, SelectionKey.OP_READ)

                println("Client connected: ${client.remoteAddress}")
                println("Client request URL: ${clientKey.attachment()}")

                try {
                    val server = SocketChannel.open(InetSocketAddress(ip, ipPort))
                        .configureBlocking(false)
                        .register(selector, SelectionKey.OP_READ)

                    // store the server channel in the client channel and wise versa
                    server.attach(clientKey)
                    clientKey.attach(server)
                } catch (e: Exception) {
                    println("Error connecting to server: ${e.message}")
                    client.close()
                }
            }
            if (key.isReadable) {
                val buffer = ByteBuffer.allocate(1024)
                val client = key.channel() as SocketChannel
                // We have a pair: client <-> server
                val server = ((key.attachment() as SelectionKey).channel() as SocketChannel)

                try {
                    if (!client.isOpen || !server.isOpen) {
                       throw Exception("Channel is closed")
                    }
                    if (client.read(buffer) == -1) {
                        throw Exception("Client closed connection")
                    }
                    buffer.flip()
                    server.write(buffer)
                } catch (e: Exception) {
                    println("Error writing to server: ${e.message}")
                    client.close()
                    server.close()
                }
            }
        }
    }
}
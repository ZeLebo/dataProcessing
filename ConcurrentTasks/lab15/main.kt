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

    val selector = Selector.open()
    val serverSocketChanel = ServerSocketChannel.open()
    serverSocketChanel.bind(InetSocketAddress("localhost", port))
    serverSocketChanel.configureBlocking(false)
    serverSocketChanel.register(selector, SelectionKey.OP_ACCEPT)

    while (true) {
        selector.select()
        val selectedKeys = selector.selectedKeys()
        val iterator = selectedKeys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            if (key.isAcceptable) {
                val conn = serverSocketChanel.accept()
                conn.configureBlocking(false)
                conn.register(selector, SelectionKey.OP_READ)
                println("Go new connection from ${conn.remoteAddress}")
            }
            if (key.isReadable) {
                val buffer = ByteBuffer.allocate(1024)
                val client = key.channel() as SocketChannel
                try{
                    val n = client.read(buffer)
                    if (n < 0) {
                        client.close()
                    } else {
                        buffer.flip()
                        val data = ByteArray(buffer.limit())
                        buffer.get(data)
                        println(String(data))
                        // write the response to the client
                        client.write(ByteBuffer.wrap("HTTP/1.1 200 OK\r\n\r\n".toByteArray()))
                        client.write(ByteBuffer.wrap("Hello World".toByteArray()))
                        client.close()
                    }
                } catch (ignored:Exception){
                    client.write(ByteBuffer.wrap("HTTP/1.1 500 Internal Server Error\r\n\r\n".toByteArray()))
                    client.close()
                }
            }
            iterator.remove()
        }
        println("iteration")
    }
}
package selectserver

import java.net.ServerSocket
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

fun main(args: Array<String>) {

    EchoServer().main(args)
}

// using select and poll
class EchoServer {
    private val POISON_PILL = "POISON_PILL"

    fun answerWithEcho(buffer: ByteBuffer, key: SelectionKey) {
        val channel = key.channel() as SocketChannel
        buffer.flip()
        channel.write(buffer)
        buffer.compact()

        if (buffer.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ or SelectionKey.OP_WRITE)
        } else {
            key.interestOps(SelectionKey.OP_READ)
        }
    }

    fun register(selector: Selector, channel: SocketChannel) {
        channel.configureBlocking(false)
        channel.register(selector, SelectionKey.OP_READ)
    }

    // public static Process start() throws IOException {
    fun start(port: Int, ip: String, ipPort: Int): Process {
        return ProcessBuilder(
            "java",
            "-cp",
            "out/production/SelectServer",
            "selectserver.EchoServer",
            port.toString(),
            ip,
            ipPort.toString()
        ).start() ?: throw RuntimeException("Could not start process")

//        val process = Runtime.getRuntime().exec("nc -l $port")
//        val inputStream = process.inputStream
//        val outputStream = process.outputStream
//
//        val selector = Selector.open()
//        val serverSocketChannel = ServerSocketChannel.open()
//        serverSocketChannel.configureBlocking(false)
//        val serverSocket = serverSocketChannel.socket()
//        serverSocket.bind(InetSocketAddress(ip, ipPort))
//        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT)
//
//        val buffer = ByteBuffer.allocate(1024)
//        while (true) {
//            selector.select()
//            val keys = selector.selectedKeys()
//            val iterator = keys.iterator()
//            while (iterator.hasNext()) {
//                val key = iterator.next()
//                iterator.remove()
//                if (key.isAcceptable()) {
//                    val server = key.channel() as ServerSocketChannel
//                    val client = server.accept()
//                    register(selector, client)
//                } else if (key.isReadable()) {
//                    val channel = key.channel() as SocketChannel
//                    val read = channel.read(buffer)
//                    if (read == -1) {
//                        channel.close()
//                    } else {
//                        answerWithEcho(buffer, key)
//                    }
//                } else if (key.isWritable()) {
//                    answerWithEcho(buffer, key)
//                }
//            }
//        }
//        return ProcessBuilder("nc", "-l", "9090").start() ?: throw IOException("Process is null")
    }

    fun main(args : Array<String>) {
        val port = if (args.isNotEmpty()) args[0].toInt() else 9090
        val ip = if (args.size > 1) args[1] else "localhost"
        val ipPort = if (args.size > 2) args[2].toInt() else 80

        val selector = Selector.open()
        val serverSocket = ServerSocket(9090)

        if (serverSocket.channel == null) {
            throw RuntimeException("Could not open server socket channel")
        }

        val serverChannel = serverSocket.channel
        serverChannel.configureBlocking(false)
        serverChannel.register(selector, SelectionKey.OP_ACCEPT)

        val buffer = ByteBuffer.allocate(1024)
        while (true) {
            selector.select()
            val keys = selector.selectedKeys()
            val iterator = keys.iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                iterator.remove()
                if (key.isAcceptable()) {
                    val server = key.channel() as ServerSocketChannel
                    val client = server.accept()
                    register(selector, client)
                } else if (key.isReadable()) {
                    val channel = key.channel() as SocketChannel
                    val read = channel.read(buffer)
                    if (read == -1) {
                        channel.close()
                    } else {
                        answerWithEcho(buffer, key)
                    }
                } else if (key.isWritable()) {
                    answerWithEcho(buffer, key)
                }
            }
        }
    }
}

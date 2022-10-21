package lab12

import java.util.*
import java.util.concurrent.locks.ReentrantLock


fun main() {
    val storage = LinkedList<String>()
    val mutex = ReentrantLock()
    Thread {
        while (true) {
            val input = readLine()
            if (input!!.isEmpty()) {
                println(storage.joinToString("\n"))
            } else if (input == "exit") {
                for (thread in Thread.getAllStackTraces().keys) {
                    if (thread.name == "Sync") {
                        thread.interrupt()
                        break
                    }
                }
                break
            } else {
                // or synchronized(storage) { storage.add(input) }
                mutex.lock()
                storage.add(0, input)
                mutex.unlock()
            }
        }
    }.start()

    Thread {
        Thread.currentThread().name = "Sync"
        try {
            while (true) {
                Thread.sleep(5000)
                mutex.lock()
                if (storage.isNotEmpty()) {
                    for (i in 0 until storage.size - 1) {
                        for (j in 0 until storage.size - i - 1) {
                            if (storage[j] > storage[j + 1]) {
                                val temp = storage[j]
                                storage[j] = storage[j + 1]
                                storage[j + 1] = temp
                            }
                        }
                    }
                    mutex.unlock()
                }
            }
        } catch (e: InterruptedException) {
            println("Sync thread was interrupted")
        }
    }.start()

    println("Enter a string to add to the list")
    println("Enter an empty line to print the list")
    println("Enter 'exit' to exit")
}
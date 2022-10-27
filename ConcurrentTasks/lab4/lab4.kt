package org.example;

fun main() {
    val thread = Thread {
        var i = 0
        while (true) {
            println("hello "+i++)
            Thread.sleep(0)
        }
    }
    thread.start()
    thread.interrupt()
    println("thread finished")
}
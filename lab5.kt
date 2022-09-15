package org.example;

fun main() {
    val thread = Thread {
        var i = 0
        
        // print "hi", catch interrupt, print "bye"
        while (true) {
            try {
                println("hi $i"); i++
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                println("thread has been interrupted")
                Thread.sleep(1000)
                println("bye")
            }
        }
    }

    thread.start()
    Thread.sleep(5000)
    println("Interrupting the thread")
    Thread.sleep(500)
    thread.interrupt()
    println("Waiting for the thread to finish")
}
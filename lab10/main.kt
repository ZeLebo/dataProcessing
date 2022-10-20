package org.example

// write string from main and thread by queue using mutex
fun main() {
    val lock = Object()
    var switcher = false
    // вообще тут должен быть mutex но он не импортировался
    Thread {
        for (i in 1..10) {
            synchronized(lock) {
                while(!switcher) {
                    lock.wait() 
                }
            }

            println("Second thread $i")

            synchronized(lock) {            
                switcher = false
                lock.notifyAll()
            }
        }
    }.start()

    for (i in 1..10) {
        synchronized(lock) {
            while(switcher) {
                lock.wait()
            }
        }

        println("Main thread $i")

        synchronized(lock) {
            switcher = true
            lock.notifyAll()
        }
    }
}
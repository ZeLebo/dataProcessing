package lab11

import java.util.concurrent.Semaphore

fun main() {
    // block the thread, when acquiring the semaphore
    // zero semaphore is blocking by default, need to release to use
    val sem1 = Semaphore(1)
    val sem2 = Semaphore(0)

    Thread {
        for (i in 1..10) {
            sem1.acquire()
            print("Ping - ")
            sem2.release()
        }
    }.start()

    for (i in 1 .. 10) {
        sem2.acquire()
        println("Pong $i")
        sem1.release()
    }
}
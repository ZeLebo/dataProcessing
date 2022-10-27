package lab10.mutexes

import java.util.concurrent.locks.ReentrantLock

// printing string in a row with lock
fun main() {
    val mutex = ReentrantLock()
    val asyncCondition = mutex.newCondition()

    fun sendMessage(i : Int) {
        println("$i message from ${Thread.currentThread().name}")
    }
    fun work(i : Int) {
        mutex.lock()
        sendMessage(i)

        // send the signal to the second thread to send the message
        asyncCondition.signal()
        // await the signal from the second thread
        asyncCondition.await()
        mutex.unlock()
    }
    // needed to use Mutex
    Thread {
        Thread.currentThread().name = "Child thread"
        for (i in 1..10) {
            work(i)
        }
    }.start()

    for (i in 1..10) {
        work(i)
    }

    mutex.lock()
    asyncCondition.signalAll()
    mutex.unlock()
}
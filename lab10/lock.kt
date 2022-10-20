package lab10

import java.util.concurrent.locks.ReentrantLock

fun main() {
    val mutex = ReentrantLock()
    val asyncCondition = mutex.newCondition()

    fun sendMessage(i : Int) {
        println("$i message from ${Thread.currentThread().name}")
    }
    fun work(i : Int) {
        mutex.lock()
        sendMessage(i)

        asyncCondition.signal()
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
    asyncCondition.signal()
    mutex.unlock()
}
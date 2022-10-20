package lab10

import java.util.concurrent.locks.ReentrantLock

fun main() {
    val mutex = ReentrantLock()
    val asyncCondition = mutex.newCondition()

    // print string in 2 threads on by one in order
    Thread {
        for (i in 1..10) {
            mutex.lock()
            println("Second thread $i")
            asyncCondition.signal()
            asyncCondition.await()
            mutex.unlock()
        }
    }.start()

    mutex.lock()
    asyncCondition.signal()
    asyncCondition.await()
    mutex.unlock()

    for (i in 1..10) {
        mutex.lock()
        println("Main thread $i")
        asyncCondition.signal()
        asyncCondition.await()
        // asyncCondition.signal()
        mutex.unlock()
    }
    
    // val lock = Object()
    // var switcher = false
    // Thread {
    //     for (i in 1..10) {
    //         synchronized(lock) {
    //             while(!switcher) {
    //                 lock.wait()
    //             }
    //         }

    //         println("Second thread $i")

    //         synchronized(lock) {
    //             switcher = false
    //             lock.notifyAll()
    //         }
    //     }
    // }.start()

    // for (i in 1..10) {
    //     synchronized(lock) {
    //         while(switcher) {
    //             lock.wait()
    //         }
    //     }

    //     println("Main thread $i")

    //     synchronized(lock) {
    //         switcher = true
    //         lock.notifyAll()
    //     }
    // }
}
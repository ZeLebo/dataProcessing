// package org.example

// fun main() {
//     val lock = Object()
//     var switcher = false
//     // вообще тут должен быть mutex но он не импортировался
//     Thread {
//         for (i in 1..10) {
//             synchronized(lock) {
//                 while(!switcher) {
//                     lock.wait()
//                 }
//             }

//             println("Second thread $i")

//             synchronized(lock) {
//                 switcher = false
//                 lock.notifyAll()
//             }
//         }
//     }.start()

//     for (i in 1..10) {
//         synchronized(lock) {
//             while(switcher) {
//                 lock.wait()
//             }
//         }

//         println("Main thread $i")

//         synchronized(lock) {
//             switcher = true
//             lock.notifyAll()
//         }
//     }
// }

// print "hi" from two threads in queue using mutex
package lab10

import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicBoolean

fun main() {
    val mutex = Semaphore(1)
    var flag = AtomicBoolean(true)
    // print from thread on by one
    Thread {
        for (i in 1..10) {
            while (!flag.get()) {
                mutex.acquire()
                flag.set(true)
                println("Second thread $i")
                mutex.release()
            }
        }
    }.start()

    Thread {
        for (i in 1..10) {
            while (flag.get()) {
                mutex.acquire()
                flag.set(false)
                println("Main thread $i")
                mutex.release()
            }
        }
    }.start()
}

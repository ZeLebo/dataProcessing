package lab14

import java.util.concurrent.Semaphore

fun main() {
    val a = Semaphore(0)
    val b = Semaphore(0)
    val c = Semaphore(0)
    val modules = Semaphore(0)

    // making a details
    var aCount = 0
    Thread {
        while(true) {
            println("started making A")
            Thread.sleep(1000)
            println("${aCount++} A has been made")
            a.release()
        }
    }.start()

    // making b details
    var bCount = 0
    Thread {
        while(true) {
            println("started making B")
            Thread.sleep(2000)
            println("${bCount++} B has been made")
            b.release()
        }
    }.start()

    // making c details
    var cCount = 0
    Thread {
        while(true) {
            println("started making C")
            Thread.sleep(3000)
            println("${cCount++} C has been made")
            c.release()
        }
    }.start()

    // making modules
    var moduleCount = 0
    Thread {
        while(true) {
            a.acquire()
            b.acquire()
            println("${moduleCount++} module has been made")
            modules.release()
        }
    }.start()

    var i = 1

    Thread {
        while(true) {
            c.acquire()
            modules.acquire()
            println("${i++} widget has been produced")
        }
    }.start()

    Runtime.getRuntime().addShutdownHook(Thread {
        println("\nThe company has been closed")
    })
}
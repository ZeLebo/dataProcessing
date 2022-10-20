package lab10.locks

fun main() {
    val lock = Object()
    var switch = false

    fun work() {
        println("Current thread is ${Thread.currentThread().name}")
        switch = !switch
        lock.notify()
    }

    Thread {
       Thread.currentThread().name = "Not main"
       for (i in 1..10) {
           synchronized(lock) {
               while (!switch) lock.wait()
               work()
           }
       }
    }.start()

    for (i in 1..10) {
        synchronized(lock) {
            while (switch) lock.wait()
            work()
        }
    }
}
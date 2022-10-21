package philosophersMatIh

import java.util.concurrent.Semaphore
class Fork(val id: Int) {
    private val semaphore = Semaphore(1)

    fun take() {
        semaphore.acquire()
    }
    fun put() {
        semaphore.release()
    }
    fun tryAcquire(): Boolean {
        return semaphore.tryAcquire()
    }
}

class Philosopher(name: String, val left: Fork, val right: Fork): Thread(name) {
    fun puttingForksDeadlock(): Boolean {
        if (!right.tryAcquire()) {
            left.put()
            Thread.sleep((Math.random() * 1000 + 2000).toLong())
                return true
        }
        return false
    }

    // making deadlock
    fun runDeadlock() {
        try {
        while (true) {
            println("$name is thinking")
            Thread.sleep(1000)
            left.take()
            println("$name took left fork id = ${left.id}")
            Thread.sleep(2000)
            // the first and naive solution to the problem
            // if the fork is not avaliable, put the fork back and wait some time
            // if (puttingForksDeadlock()) {
            //     println("$name is thinking")
            //     continue
            // }
            right.take()
            println("$name took right fork id = ${right.id}")
            println("$name is eating")
            Thread.sleep(1000)
            left.put()
            println("$name put left fork id = ${left.id}")
            right.put()
            println("$name put right fork id = ${right.id}")
        }
        } catch (e: InterruptedException) {
            println("Philosopher $name was forced to stop eating")
        }
    }

    // witout deadlock
    fun runWihoutDeadlock() {
        try {
            // deikstra solution
            // firstly take the fork with the lowest id
            // then take the other fork
            // then put the first fork
            // then put the second fork
            while (true) {
                val first = if (left.id < right.id) left else right
                val second = if (first == left) right else left

                println("$name is thinking")
                Thread.sleep(1000)
                
                // firstly take the fork with the lowest id
                // secondly take the other fork
                first.take()
                println("$name took first fork id = ${first.id}")
                second.take()
                println("$name took second fork id = ${second.id}")

                println("$name is eating")
                Thread.sleep(1000)

                // put the second fork
                // put the first fork
                second.put()
                println("$name put second fork id = ${second.id}")
                first.put()
                println("$name put first fork id = ${first.id}")
            }
        } catch (e: InterruptedException) {
            println("Phylosopher $name was forced to stop eating")
        }
    }

    override fun run() {
//        this.runWihoutDeadlock()
        this.runDeadlock()
    }
}

fun main() {
    val forks = Array(5) { Fork(it) }
    val philosophers = Array(5) {
        Philosopher("Philosopher $it", forks[it], forks[(it + 1) % 5])
    }
    philosophers.forEach { it.start() }

    // I just like this construction
    Runtime.getRuntime().addShutdownHook(Thread {
        philosophers.forEach { it.interrupt() }

        println("\nWaiting for philosophers to finish")
    })
}
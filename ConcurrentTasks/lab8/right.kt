package rightSolutionForLab
import java.util.concurrent.CyclicBarrier
import kotlin.math.pow

/*
same as the previous, making the calculations
after signal is received, need to stop the threads and print the result
*/
fun main(args: Array<String>) {
    // making it here to not repeat after
    // the amount of thread to run
    val threadsCount = if (args.isNotEmpty()) args[0].toInt() else 4
    // put the results of threads here
    val results = Array(threadsCount) { 0.0 }
    // the amount of iteration of each thread
    val iterations = Array(threadsCount) {0}
    // barrier to sync between the threads
    val barrier = CyclicBarrier(threadsCount)

    println("The calculation will be done when you press Ctrl+F2")

    usingInterrupt(threadsCount, results, iterations, barrier)
//    usingVolatile(threadsCount, results, iterations, barrier)

    // you can run both of the functions if you want, but need to twice the barrier(CyclicBarrier(var * 2))
}

// function to count the result on iteration
fun countResOnIteration(i : Int): Double {
    val value = i.toDouble()
    return (-1.0).pow(value) / (2 * value + 1)
}

fun usingInterrupt(threadsCount: Int, results: Array<Double>, iterations: Array<Int>, barrier: CyclicBarrier) {
    val threads = (0 until threadsCount).map { i ->
        Thread {
            var result = 0.0
            var iteration = i

            // count the result until the thread is interrupted
            while (!Thread.interrupted()) {
                result += countResOnIteration(iteration)
                iteration += threadsCount
            }

            // after shutdown complete the calculations
            iterations[i] = iteration
            barrier.await()

            while (iteration < iterations.max()) {
                result += countResOnIteration(iteration)
                iteration += threadsCount
            }

            results[i] = result
        }
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Caught a signal")
        threads.forEach { it.interrupt() }
        threads.forEach { it.join() }

        println("The result is ${results.sum() * 4}")
    })

    threads.forEach { it.start() }
}

//======================ANOTHER_METHOD======================
// volatile - make the variable only once
@Volatile
var stopped = false

fun usingVolatile(threadsCount: Int, results: Array<Double>, iterations: Array<Int>, barrier: CyclicBarrier) {
    val threads = (0 until threadsCount).map { i ->
        Thread {
            var result = 0.0
            var iteration = i

            // count the result until get a shutdown
            while (!stopped) {
                result += countResOnIteration(iteration)
                iteration += threadsCount
            }

            // put the max iteration of the thread to array
            iterations[i] = iteration
            // wait until all threads do the same
            barrier.await()

            // count all the iteration till the maximum
            while (iteration < iterations.max()) {
                result += countResOnIteration(iteration)
                iteration += threadsCount
            }

            results[i] = result
        }
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Caught a signal")
        stopped = true
        threads.forEach { it.join() }

        println("The result is ${results.sum() * 4}")
    })

    threads.forEach { it.start() }
}

package org.example;

fun main(args: Array<String>) {
    val threadsCount = if (args.size > 0) args[0].toInt() else Runtime.getRuntime().availableProcessors()

    var iterCount = 0
    var results = Array(threadsCount) { 0.0 }
    var threads = Array(threadsCount) { Thread() }

    fun startThreads() {
        var threadIterations = (iterCount.toDouble() / threadsCount).toInt()
        results = Array(threadsCount) { 0.0 }

        threads = (0 until threadsCount).map { i ->
            Thread {
                var result = 0.0
                for (j in (i * threadIterations) until ((i + 1) * threadIterations)) {
                    result += Math.pow(-1.0, j.toDouble()) / (2 * j + 1)
                }
                results[i] = result
            }
        }.toTypedArray()
        threads.forEach { it.start() }
    }

    fun joinThreads() {
        threads.forEach { it.join() }
        val pi = results.sum() * 4
        println()
        println("Iterations have been done: $iterCount")
        println("The result is $pi")
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        joinThreads()
    })

    println("The calculation will be done when you press Ctrl+C")

    while (true) {
        iterCount +=  1000
        startThreads()
    }
}

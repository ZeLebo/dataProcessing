package org.example;

fun main(args: Array<String>) {
    val threadsCount = if (args.size > 0) args[0].toInt() else 12
    val iterCount = if (args.size > 1) args[1].toInt() else 1000
    var threadIterations = (iterCount.toDouble() / threadsCount).toInt()
    val results = Array(threadsCount) { 0.0 }

    val threads = (0 until threadsCount).map { i ->
        Thread {
            var result = 0.0
            for (j in (i * threadIterations) until ((i + 1) * threadIterations)) {
                result += Math.pow(-1.0, j.toDouble()) / (2 * j + 1)
            }

            results[i] = result
        }
    }
    threads.forEach { it.start() }
    threads.forEach { it.join() }

    val pi = results.sum() * 4
    println(pi)
}
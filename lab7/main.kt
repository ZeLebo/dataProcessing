
// the program has to count PI number using row of Leibniz formula
// the amount of thread is given as a command line argument 
fun main(args: Array<String>) {
    // parse command line argument
    // if no given then use constant = 4
    val threadsCount = if (args.size > 0) args[0].toInt() else 12
    val iterCount = if (args.size > 1) args[1].toInt() else 1000
    var threadIterations = (iterCount.toDouble() / threadsCount).toInt()
    val results = Array(threadsCount) { 0.0 }

    // count the values of formula by iterations for each thread
    // then sum the results
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
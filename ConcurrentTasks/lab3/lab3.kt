package org.example

fun myprint(strings: List<String>) {
    strings.forEach(action = { print(it) })
    println()
}

fun main() {
    var first = listOf("fist", "1")
    var second = listOf("second", "2")
    var third = listOf("third", "3")
    var forth = listOf("forth", "4")

    val threads = listOf(first, second, third, forth).map { list ->
        Thread {
            myprint(list)
        }
    }

    threads.forEach { it.start() }
}

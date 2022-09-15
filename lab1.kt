internal object MyThread : Thread() {
    fun main(args: Array<String?>?) {
        val t = Thread { System.out.println("Hello from a thread!") }
        t.start()
    }
}

internal object lab1 {
    fun main(args: Array<String?>?) {
        for (i in 0..9) {
            System.out.println("Hello from main!")
            val t = Thread { System.out.println("Hello from a thread!") }
            t.start()
        }
    }
}
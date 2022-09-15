internal object MyThread : Thread() {
    fun main(args: Array<String?>?) {
        val t = Thread { System.out.println("Hello from a thread!") }
        t.start()
    }
}

internal object lab2 {
    fun main(args: Array<String?>?) {
        for (i in 0..9) {
            val t = Thread { System.out.println("Hello from a thread!") }
            t.start()
            try {
                t.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        System.out.println("Hello from main!")
    }
}
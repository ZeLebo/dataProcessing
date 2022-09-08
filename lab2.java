
class MyThread extends Thread {
    public static void main(String[] args) {
        Thread t = new Thread(() -> System.out.println("Hello from a thread!"));
 
        t.start();
    }
}


class lab2 {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> System.out.println("Hello from a thread!"));
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Hello from main!");
    }
}
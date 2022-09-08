class MyThread extends Thread {
    public static void main(String[] args) {
        Thread t = new Thread(() -> System.out.println("Hello from a thread!"));
 
        t.start();
    }
}


class lab1 {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("Hello from main!");
            Thread t = new Thread(() -> System.out.println("Hello from a thread!"));
            t.start();
        }
    }
}
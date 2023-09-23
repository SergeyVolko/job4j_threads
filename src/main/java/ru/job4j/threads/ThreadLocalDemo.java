package ru.job4j.threads;

public class ThreadLocalDemo {
    public static ThreadLocal<String> tl = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        Thread first = new FirstThread();
        Thread second = new SecondThread();
        tl.set("Это поток main.");
        System.out.println(tl.get());
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(tl.get());
    }
}

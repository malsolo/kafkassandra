package com.malsolo.kafkassandra.cassandra.basic;

import java.util.concurrent.CompletableFuture;

public class Sample {

    private static int generate() {
        // By commenting the next line, the entire application will run on main thread.
        // Otherwise, it'll use a ForkJoinPool
        System.out.println("Generate " + Thread.currentThread());
        return 2;
    }

    private static void printIt(int value) {
        System.out.println("Print " + value + " " + Thread.currentThread());
    }

    public static void main(String[] args) throws InterruptedException {

        CompletableFuture
            .supplyAsync(Sample::generate)
            .thenAccept(Sample::printIt);

        Thread.sleep(500);
        System.out.println("In main");
    }

}

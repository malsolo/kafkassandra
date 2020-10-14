package com.malsolo.kafkassandra.kafka.basic;

import java.util.stream.IntStream;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        IntStream.range(1, 6)
            .filter(i -> i % 2 == 0)
            .mapToObj(Integer::toString)
            .map(s -> String.format("Hi %s", s))
            .forEach(System.out::println);
    }
}

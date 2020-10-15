package com.malsolo.kafkassandra.cassandra.basic;

import static java.util.Comparator.reverseOrder;

import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
            .filter(n -> n % 2 == 0)
            .sorted(reverseOrder())
            .map(n -> "Number " + n)
            .forEach(System.out::println);
    }
}

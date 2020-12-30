package com.malsolo.kafkassandra.cassandra.basic;

import static com.malsolo.kafkassandra.cassandra.basic.CassandraQuickStart.CONTACT_POINT_1_HOST;
import static com.malsolo.kafkassandra.cassandra.basic.CassandraQuickStart.CONTACT_POINT_1_PORT;
import static com.malsolo.kafkassandra.cassandra.basic.CassandraQuickStart.DATACENTER;
import static com.malsolo.kafkassandra.cassandra.basic.CassandraQuickStart.KEYSPACE_NAME;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DriverException;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import reactor.core.publisher.Flux;

public class CassandraReactiveMain {

    //See https://docs.datastax.com/en/developer/java-driver/4.9/manual/core/reactive/
    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder()
            .addContactPoint(new InetSocketAddress(CONTACT_POINT_1_HOST, CONTACT_POINT_1_PORT))
            .withLocalDatacenter(DATACENTER)
            .withKeyspace(KEYSPACE_NAME)
            .build()) {

            var s = "select release_version from system.local";

            //noinspection ReactiveStreamsNullableInLambdaInTransform
            Flux.from(session.executeReactive(s))
                .doOnNext(System.out::println)
                .map(reactiveRow -> reactiveRow.getString("release_version"))
                .doOnNext(System.out::println)
                .blockLast();

            var stocks = "insert into stocks (symbol, date, value) values (?, ?, ?)";
            var now = Instant.now();

            Flux.just(
                ImmutablePair.of("GOOGL", BigDecimal.valueOf(1757.76)),
                ImmutablePair.of("AAPL", BigDecimal.valueOf(134.87)),
                ImmutablePair.of("BABA", BigDecimal.valueOf(236.26)),
                ImmutablePair.of("INTC", BigDecimal.valueOf(49.39)),
                ImmutablePair.of("GE", BigDecimal.valueOf(10.56)),
                ImmutablePair.of("MSFT", BigDecimal.valueOf(224.15))
            )
                .map(pair -> SimpleStatement
                    .builder(stocks)
                    .addPositionalValues(pair.getLeft(), now, pair.getRight())
                    .build())
                .doOnNext(stmt -> System.out.println(stmt.getQuery()))
                .flatMap(session::executeReactive)
                .blockLast()
            ;

            var random = new Random();
            Flux.interval(Duration.ofMillis(100))
                .map(tick -> ImmutableTriple.of("ING", Instant.now(),
                    BigDecimal.valueOf(random.nextInt(10) + Math.random())))
                .map(triple -> SimpleStatement
                    .builder(stocks)
                    .addPositionalValues(triple.getLeft(), triple.getMiddle(), triple.getRight())
                    .build())
                .doOnNext(stmt -> System.out.println(stmt.getQuery()))
                .flatMap(session::executeReactive)
                .take(10)
                .blockLast()
                ;
        } catch (DriverException de) {
            de.printStackTrace();
        }

    }

}

package com.malsolo.kafkassandra.cassandra.boot.repository;

import java.time.Instant;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;

public interface CustomizedStockRepository {

    //See https://docs.spring.io/spring-data/cassandra/docs/current/reference/html/#repositories.custom-implementations
    Flux<Stock> searchBySymbolAndDatesPaged(
        @NonNull String symbol,
        @NonNull Instant start,
        @NonNull Instant end,
        long offset,
        long limit);

}

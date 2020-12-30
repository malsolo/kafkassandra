package com.malsolo.kafkassandra.cassandra.boot.repository;

import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation;
import org.springframework.data.cassandra.repository.support.SimpleReactiveCassandraRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class StockSearchRepository extends SimpleReactiveCassandraRepository<Stock, StockKey> {

    private final ReactiveCassandraOperations operations;

    public StockSearchRepository(
        @Qualifier("stockCassandraEntityInformation")
        CassandraEntityInformation<Stock, StockKey> metadata,
        ReactiveCassandraOperations operations) {
        super(metadata, operations);
        this.operations = operations;
    }

    public Flux<Stock> searchBySymbol(
        @NonNull String symbol,
        @NonNull Instant start,
        @NonNull Instant end,
        long offset,
        long limit) {

        var stockFlux = operations.select(query(where("symbol").is(symbol))
            .and(where("date").gte(start))
            .and(where("date").lt(end)), Stock.class);

        return stockFlux.skip(offset).take(limit);
    }
}

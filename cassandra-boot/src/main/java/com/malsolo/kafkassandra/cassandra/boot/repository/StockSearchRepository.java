package com.malsolo.kafkassandra.cassandra.boot.repository;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
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

    private static final CqlIdentifier STOCKS = CqlIdentifier.fromCql("stocks");

    public static final CqlIdentifier SYMBOL = CqlIdentifier.fromCql("symbol");
    public static final CqlIdentifier DATE = CqlIdentifier.fromCql("date");
    public static final CqlIdentifier VALUE = CqlIdentifier.fromCql("value");

    private static final CqlIdentifier START = CqlIdentifier.fromCql("start");
    private static final CqlIdentifier END = CqlIdentifier.fromCql("end");

    public static final SimpleStatement FIND_STOCKS_BY_SYMBOL = QueryBuilder.selectFrom(STOCKS)
        .columns(SYMBOL, DATE, VALUE)
        .where(
            Relation.column(SYMBOL).isEqualTo(bindMarker(SYMBOL)),
            // start inclusive
            Relation.column(DATE).isGreaterThanOrEqualTo(bindMarker(START)),
            // end exclusive
            Relation.column(DATE).isLessThan(bindMarker(END))
        )
        .build();

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

        /*
        FIND_STOCKS_BY_SYMBOL.setNamedValues(Map.of(
            SYMBOL.asInternal(), symbol,
            START.asInternal(), start,
            END.asInternal(), end
        )).getQuery();

         */

        var stockFlux = operations.select(query(where("symbol").is(symbol))
            .and(where("date").gte(start))
            .and(where("date").lt(end)), Stock.class);

        return stockFlux.skip(offset).take(limit);
    }
}

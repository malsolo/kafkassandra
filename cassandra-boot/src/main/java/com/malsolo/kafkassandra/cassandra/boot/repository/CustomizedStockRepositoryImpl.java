package com.malsolo.kafkassandra.cassandra.boot.repository;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import java.time.Instant;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class CustomizedStockRepositoryImpl implements CustomizedStockRepository {

    private static final CqlIdentifier STOCKS = CqlIdentifier.fromCql("stocks");

    private static final CqlIdentifier SYMBOL = CqlIdentifier.fromCql("symbol");
    private static final CqlIdentifier DATE = CqlIdentifier.fromCql("date");
    private static final CqlIdentifier VALUE = CqlIdentifier.fromCql("value");

    private static final CqlIdentifier START = CqlIdentifier.fromCql("start");
    private static final CqlIdentifier END = CqlIdentifier.fromCql("end");

    private static final SimpleStatement SEARCH_STOCKS_BY_SYMBOL_AND_DATES = QueryBuilder.selectFrom(STOCKS)
        .columns(SYMBOL, DATE, VALUE)
        .where(
            Relation.column(SYMBOL).isEqualTo(bindMarker(SYMBOL)),
            // start inclusive
            Relation.column(DATE).isGreaterThanOrEqualTo(bindMarker(START)),
            // end exclusive
            Relation.column(DATE).isLessThan(bindMarker(END))
        )
        .build();

    private final CqlSession cqlSession;

    @Override
    public Flux<Stock> searchBySymbolAndDatesPaged(
        @NonNull String symbol,
        @NonNull Instant start,
        @NonNull Instant end,
        long offset, long limit) {
        var preparedSearch = cqlSession.prepare(SEARCH_STOCKS_BY_SYMBOL_AND_DATES);
        var boundSearch = preparedSearch.bind(symbol, start, end);
        var reactiveResultSet = cqlSession.executeReactive(boundSearch);
        var flux = Flux.from(reactiveResultSet);
        return flux.skip(offset).take(limit)
            .map(row -> new Stock(
                new StockKey(
                    Objects.requireNonNull(row.getString(SYMBOL), "column symbol cannot be null"),
                    Objects.requireNonNull(row.getInstant(DATE), "column date cannot be null")
                ),
                Objects.requireNonNull(row.getBigDecimal(VALUE), "column value cannot be null")
            ));
    }
}

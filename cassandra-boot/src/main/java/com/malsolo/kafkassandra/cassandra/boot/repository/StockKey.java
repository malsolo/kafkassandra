package com.malsolo.kafkassandra.cassandra.boot.repository;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockKey implements Serializable {

    @PrimaryKeyColumn(
        name = "symbol",
        ordinal = 0,
        type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = Name.TEXT)
    private String symbol;

    @PrimaryKeyColumn(
        name = "date",
        type = PrimaryKeyType.CLUSTERED,
        ordering = Ordering.DESCENDING)
    @CassandraType(type = Name.TIMESTAMP)
    private Instant date;

}

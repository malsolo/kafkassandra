package com.malsolo.kafkassandra.cassandra.boot.repository;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("stocks")
public class Stock {
    @PrimaryKey
    private StockKey key;

    @Column("value")
    @CassandraType(type = Name.DECIMAL)
    private BigDecimal value;
}

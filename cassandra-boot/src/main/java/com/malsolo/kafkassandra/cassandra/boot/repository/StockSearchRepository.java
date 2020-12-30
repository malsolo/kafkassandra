package com.malsolo.kafkassandra.cassandra.boot.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation;
import org.springframework.data.cassandra.repository.support.SimpleReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StockSearchRepository extends SimpleReactiveCassandraRepository<Stock, StockKey> {

    public StockSearchRepository(
        @Qualifier("stockCassandraEntityInformation")
        CassandraEntityInformation<Stock, StockKey> metadata,
        ReactiveCassandraOperations operations) {
        super(metadata, operations);
    }
}

package com.malsolo.kafkassandra.cassandra.boot.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface StockRepository extends ReactiveCassandraRepository<Stock, StockKey> {
}

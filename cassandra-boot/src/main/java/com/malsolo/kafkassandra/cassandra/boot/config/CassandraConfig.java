package com.malsolo.kafkassandra.cassandra.boot.config;

import com.malsolo.kafkassandra.cassandra.boot.repository.Stock;
import com.malsolo.kafkassandra.cassandra.boot.repository.StockKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentEntity;
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation;
import org.springframework.data.cassandra.repository.support.MappingCassandraEntityInformation;

@Configuration
public class CassandraConfig {

    @Bean
    public CassandraEntityInformation<Stock, StockKey> stockCassandraEntityInformation(CassandraOperations cassandraTemplate) {
        @SuppressWarnings("unchecked")
        BasicCassandraPersistentEntity<Stock> entity = (BasicCassandraPersistentEntity<Stock>) cassandraTemplate
            .getConverter()
            .getMappingContext()
            .getRequiredPersistentEntity(Stock.class);

        return new MappingCassandraEntityInformation<>(entity, cassandraTemplate.getConverter());
    }

}

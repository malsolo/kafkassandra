package com.malsolo.kafkassandra.cassandra.boot.repository;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void should_read_perform_crud_operations() {
        //Given
        StockKey stockKey = new StockKey("FAKE", Instant.now());
        Stock stock = new Stock(stockKey, BigDecimal.valueOf(1.0));

        //When
        var saved = stockRepository.save(stock);
        //Then
        StepVerifier.create(saved)
            .expectNextMatches(stockSaved -> stockKey.equals(stockSaved.getKey()))
            .verifyComplete();

        //When
        var stockById = stockRepository.findById(stockKey);
        //Then
        StepVerifier.create(stockById)
            .expectNextMatches(stockFound ->
                stockKey.getSymbol().equals(stockFound.getKey().getSymbol()) &&
                stockKey.getDate().toEpochMilli() == stockFound.getKey().getDate().toEpochMilli())
            .verifyComplete();

        //When
        var deleted = stockRepository.delete(stock);
        //Then
        StepVerifier.create(deleted)
            .expectNextCount(0)
            .verifyComplete();
    }

}

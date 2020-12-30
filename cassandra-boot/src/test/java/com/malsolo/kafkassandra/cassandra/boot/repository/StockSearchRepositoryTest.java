package com.malsolo.kafkassandra.cassandra.boot.repository;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class StockSearchRepositoryTest {

    @Autowired
    private StockSearchRepository stockSearchRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void should_read_perform_crud_operations() {
        //Given
        StockKey stockKey = new StockKey("SRCH", Instant.now());
        Stock stock = new Stock(stockKey, BigDecimal.valueOf(2.0));

        //When
        var saved = stockSearchRepository.save(stock);
        //Then
        StepVerifier.create(saved)
            .expectNextMatches(stockSaved -> stockKey.equals(stockSaved.getKey()))
            .verifyComplete();


        //When
        var stockById = stockSearchRepository.findById(stockKey);
        //Then
        StepVerifier.create(stockById)
            .expectNextMatches(stockFound ->
                stockKey.getSymbol().equals(stockFound.getKey().getSymbol()) &&
                    stockKey.getDate().toEpochMilli() == stockFound.getKey().getDate().toEpochMilli())
            .verifyComplete();

        //When
        var deleted = stockSearchRepository.delete(stock);
        //Then
        StepVerifier.create(deleted)
            .expectNextCount(0)
            .verifyComplete();
    }


}

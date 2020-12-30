package com.malsolo.kafkassandra.cassandra.boot.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
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

    @Test
    void should_return_paged_flux_when_searching_by_symbol() {
        var symbol = "FIND";
        var offset = 10;
        var limit = 5;

        var now = LocalDateTime.now()
            .withHour(12).withMinute(0).withSecond(0).withNano(0);
        var to = now.plusMinutes(2);

        var zone = ZoneId.systemDefault();

        var stockFlux = Flux.range(0, 20)
            .map(now::withSecond)
            .map(ldt ->
                new Stock(
                    new StockKey(symbol, ldt.atZone(zone).toInstant()),
                    BigDecimal.valueOf(ldt.getSecond()))
            );

        var stockFluxSaved = stockFlux
            .flatMap(stockSearchRepository::save);

        StepVerifier.create(stockFluxSaved)
            .expectNextCount(20)
            .verifyComplete();

        var stockFluxFound = stockSearchRepository.searchBySymbol(symbol,
            now.atZone(ZoneId.systemDefault()).toInstant(),
            to.atZone(ZoneId.systemDefault()).toInstant(),
            offset, limit);

        StepVerifier.create(stockFluxFound)
            .expectNextMatches(stock ->
                symbol.equals(stock.getKey().getSymbol()) &&
                now.plusSeconds(offset - 1).equals(LocalDateTime.ofInstant(stock.getKey().getDate(), zone)))
            .expectNextCount(limit - 2)
            .expectNextMatches(stock ->
                symbol.equals(stock.getKey().getSymbol()) &&
                now.plusSeconds(offset - limit).equals(LocalDateTime.ofInstant(stock.getKey().getDate(), zone)))
            .verifyComplete();

        var stockFluxDeleted = stockFlux
            .flatMap(stockSearchRepository::delete);

        StepVerifier.create(stockFluxDeleted)
            .expectNextCount(0)
            .verifyComplete();
    }


}

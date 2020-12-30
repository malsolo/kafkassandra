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

    @Test
    void should_return_paged_flux_when_searching_by_symbol() {
        var symbol = "LOOK";
        var offset = 1;
        var limit = 3;

        var now = LocalDateTime.now()
            .withHour(10).withMinute(0).withSecond(0).withNano(0);

        var zone = ZoneId.systemDefault();

        var stockFlux = Flux.just(
            new Stock(new StockKey(symbol, now.plusSeconds(1).atZone(zone).toInstant()), new BigDecimal("1.0")),
            new Stock(new StockKey(symbol, now.plusSeconds(2).atZone(zone).toInstant()), new BigDecimal("2.0")),
            new Stock(new StockKey(symbol, now.plusSeconds(3).atZone(zone).toInstant()), new BigDecimal("3.0")),
            new Stock(new StockKey(symbol, now.plusSeconds(4).atZone(zone).toInstant()), new BigDecimal("4.0")),
            new Stock(new StockKey(symbol, now.plusSeconds(5).atZone(zone).toInstant()), new BigDecimal("5.0"))
        );

        var to = now.plusSeconds(6);

        var stockFluxSaved = stockFlux
            .flatMap(stockRepository::save);

        StepVerifier.create(stockFluxSaved)
            .expectNextCount(5)
            .verifyComplete();

        var stockFluxFound = stockRepository.searchBySymbolAndDatesPaged(symbol,
            now.atZone(ZoneId.systemDefault()).toInstant(),
            to.atZone(ZoneId.systemDefault()).toInstant(),
            offset, limit);

        StepVerifier.create(stockFluxFound)
            .expectNextMatches(stock ->
                symbol.equals(stock.getKey().getSymbol()) &&
                    now.plusSeconds(4).equals(LocalDateTime.ofInstant(stock.getKey().getDate(), zone)))
            .expectNextCount(1)
            .expectNextMatches(stock ->
                symbol.equals(stock.getKey().getSymbol()) &&
                now.plusSeconds(2).equals(LocalDateTime.ofInstant(stock.getKey().getDate(), zone)))
            .expectNextCount(0)
            .verifyComplete();

        var stockFluxDeleted = stockFlux
            .flatMap(stockRepository::delete);

        StepVerifier.create(stockFluxDeleted)
            .expectNextCount(0)
            .verifyComplete();
    }


}

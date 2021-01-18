package com.malsolo.kafkassandra.kafka.streams.purchase.clients.producer;

import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.TRANSACTIONS_TOPIC_SOURCE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.malsolo.kafkassandra.kafka.streams.purchase.clients.datagen.Generator;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;
import com.malsolo.kafkassandra.kafka.streams.purchase.serde.JsonSerializer;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

@Slf4j
public class MockDataProducer {

    public static final String BOOTSTRAP_SERVERS = "localhost:9092";
    public static final int NUMBER_OF_PURCHASES = 1;

    private static ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    private static ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        log.info("MockDataProducer...");

        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        properties.put(ProducerConfig.RETRIES_CONFIG, "3");

        var latch = new CountDownLatch(NUMBER_OF_PURCHASES);
        var count = new AtomicInteger(0);

        var stringSerializer = new StringSerializer();
        var purchaseJsonSerializer = new JsonSerializer<Purchase>();

        try (var producer = new KafkaProducer<>(properties, stringSerializer, purchaseJsonSerializer)) {
            log.info("About to produce purchases...");
            SCHEDULER.scheduleWithFixedDelay(() -> {
                log.info("Produce purchases...");
                var purchase = Generator.generatePurchase();
                var purchaseJson = generateJson(purchase);
                var purchaseRecord = new ProducerRecord<String, Purchase>(TRANSACTIONS_TOPIC_SOURCE, null, purchase);
                log.info("Purchase created {}", purchaseJson);

                producer.send(purchaseRecord, ((metadata, exception) -> {
                    if (exception == null) {
                        log.info("Purchase record sent successfully, to topic {}, partition {}, and offset {}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                    } else {
                        log.error("Purchase Sent failed: {}", exception.getMessage());
                        exception.printStackTrace();
                    }
                }));

                log.info("Purchase produced {}.", count.incrementAndGet());
                latch.countDown();
            }, 500, 200, TimeUnit.MILLISECONDS);
            log.info("Waiting to purchases...");
            latch.await();
            log.info("End Waiting to purchases.");
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("MockDataProducer. End.");
            SCHEDULER.shutdown();
        }));

        log.info("MockDataProducer. Done.");
        System.exit(0);
    }

    private static String generateJson(Purchase purchase) {
        try {
            return MAPPER.writeValueAsString(purchase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error writing the object to String ", e);
        }
    }

}

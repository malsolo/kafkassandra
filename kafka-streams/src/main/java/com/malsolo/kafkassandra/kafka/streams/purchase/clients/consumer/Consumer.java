package com.malsolo.kafkassandra.kafka.streams.purchase.clients.consumer;

import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.AMUSEMENT_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.ELECTRONICS_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.PATTERNS_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.PURCHASES_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.REWARDS_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.serde.JsonDeserializer.CONFIG_KEY_JSON_CLASS;

import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.PurchasePattern;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.RewardAccumulator;
import com.malsolo.kafkassandra.kafka.streams.purchase.serde.JsonDeserializer;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

@Slf4j
public class Consumer {

    public static final String BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String CLIENT_ID = "purchasesConsumer";
    public static final String GROUP_ID = "purchasesConsumerGroup";
    public static final String AUTO_OFFSET_RESET = "earliest";

    public static final int PURCHASES_TOPIC_SINK_PARTITIONS = 1;
    public static final int PATTERNS_TOPIC_SINK_PARTITIONS = 2;
    public static final int REWARDS_TOPIC_SINK_PARTITIONS = 3;
    public static final int AMUSEMENT_TOPIC_SINK_PARTITIONS = 1;
    public static final int ELECTRONICS_TOPIC_SINK_PARTITIONS = 1;

    public static void main(String[] args) throws InterruptedException {
        var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET);

        log.info("Topology consumer");

        var executorService = Executors.newFixedThreadPool(
            PURCHASES_TOPIC_SINK_PARTITIONS +
                PATTERNS_TOPIC_SINK_PARTITIONS +
                REWARDS_TOPIC_SINK_PARTITIONS +
                AMUSEMENT_TOPIC_SINK_PARTITIONS +
                ELECTRONICS_TOPIC_SINK_PARTITIONS
        );

        var latch = new CountDownLatch(
            1); //Currently we are waiting only for killterm having runnables with infinite loop

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Topology consumer TERMINATED.");
            executorService.shutdown();
            latch.countDown();
        }));

        var purchaseRunner = new Runner<>(new Purchase(), "PURCHASES");
        var purchasePatternRunner = new Runner<>(new PurchasePattern(), "PATTERNS");
        var rewardAccumulatorRunner = new Runner<>(new RewardAccumulator(), "REWARDS");
        var amusementRunner = new Runner<>(new Purchase(), "AMUSEMENTS");
        var electronicsRunner = new Runner<>(new Purchase(), "ELECTRONICS");

        executorService.submit(purchaseRunner.consumer(props, PURCHASES_TOPIC_SINK, "purchaseConsumer1"));

        executorService.submit(purchasePatternRunner.consumer(props, PATTERNS_TOPIC_SINK, "purchasePatternConsumer1"));
        executorService.submit(purchasePatternRunner.consumer(props, PATTERNS_TOPIC_SINK, "purchasePatternConsumer2"));

        executorService.submit(rewardAccumulatorRunner.consumer(props, REWARDS_TOPIC_SINK, "rewardAccumulatorConsumer1"));
        executorService.submit(rewardAccumulatorRunner.consumer(props, REWARDS_TOPIC_SINK, "rewardAccumulatorConsumer2"));
        executorService.submit(rewardAccumulatorRunner.consumer(props, REWARDS_TOPIC_SINK, "rewardAccumulatorConsumer3"));

        executorService.submit(amusementRunner.consumer(props, AMUSEMENT_TOPIC_SINK, "amusementConsumer1"));
        executorService.submit(electronicsRunner.consumer(props, ELECTRONICS_TOPIC_SINK, "electronicsConsumer1"));

        latch.await();

        log.info("Topology consumer END.");
    }

    private static class Runner<T> {

        private final T t;
        private final String name;

        public Runner(T t, String name) {
            this.t = t;
            this.name = name;
        }

        @SuppressWarnings("InfiniteLoopStatement")
        public Runnable consumer(Properties props, String topic, String clientId) {
            return () -> {
                log.info("····· Consume {}.", name);
                var stringDeserializer = new StringDeserializer();
                var jsonDeserializer = new JsonDeserializer<T>();
                jsonDeserializer.configure(Map.of(CONFIG_KEY_JSON_CLASS, t.getClass()), false);

                var properties = new Properties();
                properties.putAll(props);
                properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);

                try (var consumer = new KafkaConsumer<>(props, stringDeserializer, jsonDeserializer)) {

                    consumer.subscribe(Collections.singletonList(topic));

                    while (true) {
                        ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));

                        for (ConsumerRecord<String, T> record : records) {
                            var label = name.toUpperCase();

                            log.info("Consumed {} from Kafka: topic {}, partition {}, offset {}, and key {}",
                                label, record.topic(), record.partition(), record.offset(), record.key());

                            log.info("{} Key: {}, Value: {}", label, record.key(), record.value());
                        }
                    }
                }
            };
        }
    }
}

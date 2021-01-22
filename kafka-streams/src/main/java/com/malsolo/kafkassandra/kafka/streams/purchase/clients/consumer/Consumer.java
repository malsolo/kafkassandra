package com.malsolo.kafkassandra.kafka.streams.purchase.clients.consumer;

import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.AMUSEMENT_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.CORRELATED_PURCHASES_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.ELECTRONICS_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.PATTERNS_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.PURCHASES_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.REWARDS_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.serde.JsonDeserializer.CONFIG_KEY_JSON_CLASS;

import com.malsolo.kafkassandra.kafka.streams.purchase.model.CorrelatedPurchase;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.PurchasePattern;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.RewardAccumulator;
import com.malsolo.kafkassandra.kafka.streams.purchase.serde.JsonDeserializer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;
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
    public static final int CORRELATED_PURCHASES_TOPIC_SINK_PARTITIONS = 1;

    public static void main(String[] args) throws InterruptedException {
        var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        log.info("Topology consumer");

        var executorService = Executors.newFixedThreadPool(
            PURCHASES_TOPIC_SINK_PARTITIONS +
                PATTERNS_TOPIC_SINK_PARTITIONS +
                REWARDS_TOPIC_SINK_PARTITIONS +
                AMUSEMENT_TOPIC_SINK_PARTITIONS +
                ELECTRONICS_TOPIC_SINK_PARTITIONS +
                CORRELATED_PURCHASES_TOPIC_SINK_PARTITIONS
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
        var correlatedPurchasesRunner = new Runner<>(new CorrelatedPurchase(), "CORRELATED_PURCHASES");

        executorService.submit(purchaseRunner.consumer(props, PURCHASES_TOPIC_SINK, "purchaseConsumer1"));

        executorService.submit(purchasePatternRunner.consumer(props, PATTERNS_TOPIC_SINK, "purchasePatternConsumer1"));
        executorService.submit(purchasePatternRunner.consumer(props, PATTERNS_TOPIC_SINK, "purchasePatternConsumer2"));

        executorService.submit(rewardAccumulatorRunner.consumer(props, REWARDS_TOPIC_SINK, "rewardAccumulatorConsumer1"));
        executorService.submit(rewardAccumulatorRunner.consumer(props, REWARDS_TOPIC_SINK, "rewardAccumulatorConsumer2"));
        executorService.submit(rewardAccumulatorRunner.consumer(props, REWARDS_TOPIC_SINK, "rewardAccumulatorConsumer3"));

        executorService.submit(amusementRunner.consumer(props, AMUSEMENT_TOPIC_SINK, "amusementConsumer1"));
        executorService.submit(electronicsRunner.consumer(props, ELECTRONICS_TOPIC_SINK, "electronicsConsumer1"));
        executorService.submit(correlatedPurchasesRunner.consumer(props, CORRELATED_PURCHASES_TOPIC_SINK, "correlatedPurchasesConsumer1"));

        latch.await();

        log.info("Topology consumer END.");
    }

    private static class Runner<T> {

        private static final String GROUP_ID_SUFFIX = "_GROUP";
        private static final String GROUP_ID_PREFFIX = "KSIA_";

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
                properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_PREFFIX + this.name + GROUP_ID_SUFFIX);

                var consumer = new KafkaConsumer<>(properties, stringDeserializer, jsonDeserializer);
                try {

                    consumer.subscribe(Collections.singletonList(topic));

                    while (true) {
                        ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));

                        for (ConsumerRecord<String, T> record : records) {
                            var label = name.toUpperCase();

                            log.info("Consumed {} from Kafka: topic {}, partition {}, offset {}, and key {}",
                                label, record.topic(), record.partition(), record.offset(), record.key());

                            log.info("{} Key: {}, Value: {}", label, record.key(), record.value());
                        }
                        commitCurrentOffset(consumer);
                    }
                } catch (SerializationException se) {
                    log.error(se.getMessage());
                    var error = obtainErrorMessage(se);
                    publishErrorToDLQ(error);
                    System.err.println(error);
                    //TODO: try to seek to skip the poison pill
                    //consumer.seek(new TopicPartition("", 0), 1);
                }
                finally {
                    consumer.close();
                }
            };
        }

        private void commitCurrentOffset(KafkaConsumer<String, T> consumer) {
            consumer.commitAsync((offsets, exception) -> {
                if (exception != null) {
                    log.error("Error committing current offset for {}: {}", name, exception.getMessage());
                } else {
                    log.trace("Commit current offset for {}: {}", name,
                        offsets.keySet().stream()
                            .map(tp ->
                                String.format("\n\t topic %s partition %d offset %d metadata %s",
                                    tp.topic(), tp.partition(),
                                    offsets.get(tp).offset(), offsets.get(tp).metadata()))
                            .collect(Collectors.joining(",\n"))
                    );
                }
            });
        }

        private void publishErrorToDLQ(String error) {
            System.err.printf("Publish error %s to DLQ\n", error);
        }

        private String obtainErrorMessage(Exception e) {
            var sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }
}

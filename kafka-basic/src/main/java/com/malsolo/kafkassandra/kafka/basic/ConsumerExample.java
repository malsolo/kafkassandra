package com.malsolo.kafkassandra.kafka.basic;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerExample {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerExample.class);
    public static final String GROUP_ID_CONFIG_VALUE = "demo-consumer-1";
    public static final String AUTO_OFFSET_RESET_CONFIG_VALUE = "earliest";

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Please provide command line arguments: configPath topic");
            System.exit(1);
        }

        var configFile = args[0];
        var topic = args[1];

        var props = KafkaAdminUtil.loadConfig(configFile);

        addAdditionalProperties(props);

        try (var consumer = new KafkaConsumer<Integer, String>(props)) {
            consumer.subscribe(Collections.singletonList(topic));
            while (true) {
                var records = consumer.poll(Duration.ofMillis(100));
                for (var record: records) {
                    logger.info("Consumed from Kafka: topic {}, partition {}, offset {}",
                        record.topic(), record.partition(), record.offset());

                    logger.info("Key: {}, Value: {}", record.key(), record.value());
                }
            }
        }
    }

    private static void addAdditionalProperties(Properties props) {
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG_VALUE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET_CONFIG_VALUE);
    }

}

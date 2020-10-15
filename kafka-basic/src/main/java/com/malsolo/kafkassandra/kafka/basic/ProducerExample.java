package com.malsolo.kafkassandra.kafka.basic;

import java.io.IOException;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerExample {

    public static final String ACKS_CONFIG_VALUE = "all";

    private static final Logger logger = LoggerFactory.getLogger(ProducerExample.class);

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Please provide command line arguments: configPath topic numMessages");
            System.exit(1);
        }

        var configFile = args[0];
        var topic = args[1];
        var numMessages = Integer.parseInt(args[2]);

        var props = KafkaAdminUtil.loadConfig(configFile);

        KafkaAdminUtil.createTopic(topic, 1, 1, props);
        
        addAdditionalProperties(props);

        var producer = new KafkaProducer<Integer, String>(props);
        logger.info("About to send {} messages to topic {}", numMessages, topic);
        for (int key = 0; key < numMessages; key++) {
            String message = "Message_" + key;
            producer.send(new ProducerRecord<>(topic, key, message), (metadata, exception) -> {
                if (exception == null) {
                    logger.info("Message sent successfully, to topic {}, partition {}, and offset {}",
                        metadata.topic(), metadata.partition(), metadata.offset());
                }
                else {
                    logger.error("Send failed", exception);
                }
            });
        }

        producer.flush();

        logger.info("{} messages were produced to topic {}", numMessages,  topic);

        producer.close();
    }

    private static void addAdditionalProperties(Properties props) {
        props.put(ProducerConfig.ACKS_CONFIG, ACKS_CONFIG_VALUE);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    }

}

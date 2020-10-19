package com.malsolo.kafkassandra.kafka.boot;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaBootConsumer {

    @KafkaListener(id = "KafkaBootConsumer",
        autoStartup = "${kafka.consumer.auto-start}",
        topics = "${kafka.consumer.topic}",
        groupId = "${kafka.consumer.group-id}",
        clientIdPrefix = "KafkaBootConsumer-Prefix"
    )
    public void consume(@Payload String budget,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
        @Header(KafkaHeaders.OFFSET) int offset,
        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key) {
        log.info("Received message {} from topic {}, partition {}, with offset {} and key {}",
            budget, topic, partition, offset, key);
    }

}

package com.malsolo.kafkassandra.kafka.boot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaBootProducer {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<Integer, String> kafkaTemplate;

    public void sendMessage(Integer key, String message) {
        var topic = kafkaProperties.getProducer().getTopic();
        log.info("About to send a message {} with key {} to topic {}", message, key, topic);
        kafkaTemplate.send(topic, key, message);
    }




}

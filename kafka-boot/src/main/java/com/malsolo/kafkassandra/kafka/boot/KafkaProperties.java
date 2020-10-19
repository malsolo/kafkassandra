package com.malsolo.kafkassandra.kafka.boot;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("kafka")
@Data
@AllArgsConstructor
public class KafkaProperties {

    private final Producer producer;
    private final Consumer consumer;

    @Data
    @AllArgsConstructor
    public static class Producer {
        private final boolean autoStart;
        private final String topic;
    }

    @Data
    @AllArgsConstructor
    public static class Consumer {
        private final boolean autoStart;
        private final String topic;
        private final String groupId;
    }

}

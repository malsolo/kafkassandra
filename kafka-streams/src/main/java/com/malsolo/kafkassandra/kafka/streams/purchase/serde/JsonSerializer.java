package com.malsolo.kafkassandra.kafka.streams.purchase.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

/**
 * See https://github.com/apache/kafka/blob/1.0/streams/examples/src/main/java/org/apache/kafka/streams/examples/pageview/JsonPOJOSerializer.java
 * @param <T>
 */
public class JsonSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Default constructor needed by Kafka
     */
    public JsonSerializer() {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, T data) {
        return serialize(topic, data);
    }

    @Override
    public void close() {

    }
}

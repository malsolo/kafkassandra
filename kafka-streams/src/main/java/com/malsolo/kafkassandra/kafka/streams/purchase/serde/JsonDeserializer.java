package com.malsolo.kafkassandra.kafka.streams.purchase.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * See https://github.com/apache/kafka/blob/1.0/streams/examples/src/main/java/org/apache/kafka/streams/examples/pageview/JsonPOJODeserializer.java
 * @param <T>
 */
public class JsonDeserializer<T> implements Deserializer<T> {

    public static final String CONFIG_KEY_JSON_CLASS = "JsonClass";

    private ObjectMapper objectMapper = new ObjectMapper();

    private Class<T> tClass;

    /**
     * Default constructor needed by Kafka
     */
    public JsonDeserializer() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        tClass = (Class<T>) configs.get(CONFIG_KEY_JSON_CLASS);

    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        T type;
        try {
            type = objectMapper.readValue(data, tClass);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing JSON message from topic " + topic, e);
        }

        return type;
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

    @Override
    public void close() {

    }
}

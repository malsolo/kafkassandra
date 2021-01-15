package com.malsolo.kafkassandra.kafka.streams.purchase.serde;


import static com.malsolo.kafkassandra.kafka.streams.purchase.serde.JsonDeserializer.CONFIG_KEY_JSON_CLASS;

import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.PurchasePattern;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.RewardAccumulator;
import java.util.Map;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

/**
 * Followed https://kafka.apache.org/20/documentation/streams/developer-guide/datatypes.html#json
 * Inspired by https://github.com/apache/kafka/blob/1.0/streams/examples/src/main/java/org/apache/kafka/streams/examples/pageview/PageViewTypedDemo.java
 */
public class StreamsSerdes {

    public static Serde<Purchase> PurchaseSerde() {
        var purchaseSerializer = new JsonSerializer<Purchase>();
        var purchaseDeserializer = new JsonDeserializer<Purchase>();
        purchaseDeserializer.configure(Map.of(CONFIG_KEY_JSON_CLASS, Purchase.class), false);

        return Serdes.serdeFrom(purchaseSerializer, purchaseDeserializer);
    }

    public static Serde<PurchasePattern> PurchasePatterSerde() {
        var purchasePatternSerializer = new JsonSerializer<PurchasePattern>();
        var purchasePatternDeserializer = new JsonDeserializer<PurchasePattern>();
        purchasePatternDeserializer.configure(Map.of(CONFIG_KEY_JSON_CLASS, PurchasePattern.class), false);

        return Serdes.serdeFrom(purchasePatternSerializer, purchasePatternDeserializer);
    }

    public static Serde<RewardAccumulator> RewardAccumulatorSerde() {
        var rewardAccumulatorSerializer = new JsonSerializer<RewardAccumulator>();
        var rewardAccumulatorDeserializer = new JsonDeserializer<RewardAccumulator>();
        rewardAccumulatorDeserializer.configure(Map.of(CONFIG_KEY_JSON_CLASS, RewardAccumulator.class), false);

        return Serdes.serdeFrom(rewardAccumulatorSerializer, rewardAccumulatorDeserializer);
    }

}

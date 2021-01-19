package com.malsolo.kafkassandra.kafka.streams.purchase.transformer;

import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.RewardAccumulator;
import java.util.Objects;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

public class PurchaseRewardTransformer implements ValueTransformer<Purchase, RewardAccumulator> {
    private final String storeName;
    private KeyValueStore<String, Integer> stateStore;

    public PurchaseRewardTransformer(String storeName) {
        Objects.requireNonNull(storeName,"Store Name can't be null");
        this.storeName = storeName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        this.stateStore = (KeyValueStore<String, Integer>) context.getStateStore(this.storeName);
    }

    @Override
    public RewardAccumulator transform(Purchase value) {
        var rewardAccumulator = RewardAccumulator.fromPurchase(value);

        var customerId = rewardAccumulator.getCustomerId();

        var accumulatedSoFar = this.stateStore.get(customerId);

        if (accumulatedSoFar != null) {
            rewardAccumulator.addRewardPoints(accumulatedSoFar);
        }

        this.stateStore.put(customerId, accumulatedSoFar);

        return rewardAccumulator;
    }

    @Override
    public void close() {
    }
}

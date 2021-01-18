package com.malsolo.kafkassandra.kafka.streams.purchase.partitioner;

import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;
import org.apache.kafka.streams.processor.StreamPartitioner;

public class RewardsStreamPartitioner implements StreamPartitioner<String, Purchase> {

    @Override
    public Integer partition(String topic, String key, Purchase value, int numPartitions) {
        return value.getCustomerId().hashCode() % numPartitions;
    }

}

package com.malsolo.kafkassandra.kafka.streams.purchase;

import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.AMUSEMENT_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.CUSTOMER_TRANSACTIONS_TOPIC;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.ELECTRONICS_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.EMPLOYEE_ID;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.PATTERNS_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.PURCHASES_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.REWARDS_TOPIC_SINK;
import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.TRANSACTIONS_TOPIC_SOURCE;

import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.PurchasePattern;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.RewardAccumulator;
import com.malsolo.kafkassandra.kafka.streams.purchase.partitioner.RewardsStreamPartitioner;
import com.malsolo.kafkassandra.kafka.streams.purchase.repository.PurchaseRepositorySysOut;
import com.malsolo.kafkassandra.kafka.streams.purchase.serde.StreamsSerdes;
import com.malsolo.kafkassandra.kafka.streams.purchase.transformer.PurchaseRewardTransformer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Repartitioned;
import org.apache.kafka.streams.state.Stores;

public class PurchaseTopologyApp {

    public static void main(String[] args) {

    }

    public Topology createTopology() {
        var purchaseSerde = StreamsSerdes.PurchaseSerde();
        var purchasePatternSerde = StreamsSerdes.PurchasePatterSerde();
        var rewardAccumulatorSerde = StreamsSerdes.RewardAccumulatorSerde();

        var stringSerde = Serdes.String();
        var longSerde = Serdes.Long();
        var integerSerde = Serdes.Integer();

        var builder = new StreamsBuilder();

        //1st PROCESSOR: MASKING
        var maskedPurchaseKStream = builder.stream(TRANSACTIONS_TOPIC_SOURCE,
            Consumed.with(stringSerde, purchaseSerde))
            .mapValues(Purchase::maskCreditCard);

        //FILTERING PURCHASES
        KeyValueMapper<String, Purchase, Long> purchaseDateAsKey = (key, purchase) -> purchase.getPurchaseDate().getTime();

        //Generating a Key
        KStream<Long, Purchase> filteredKStream = maskedPurchaseKStream.filter((key, purchase) -> purchase.getPrice() > 5.00).selectKey(purchaseDateAsKey);

        filteredKStream.print(Printed.<Long, Purchase>toSysOut().withLabel("purchases"));

        filteredKStream.to(PURCHASES_TOPIC_SINK,
            Produced.with(longSerde, purchaseSerde));

        //2nd PROCESSOR: PATTERNS
        var patternKStream = maskedPurchaseKStream
            .mapValues(PurchasePattern::fromPurchase);

        patternKStream.print(Printed.<String, PurchasePattern>toSysOut().withLabel("patterns"));

        patternKStream.to(PATTERNS_TOPIC_SINK,
            Produced.with(stringSerde, purchasePatternSerde));

        //3rd PROCESSOR: REWARDS

        //New feature: state store for calculating the rewards. See page 95.
        var rewardsStreamPartitioner = new RewardsStreamPartitioner();

        var transByCustomerStream = //maskedPurchaseKStream.through(CUSTOMER_TRANSACTIONS_TOPIC, Produced.with(stringSerde, purchaseSerde, rewardsStreamPartitioner));
            maskedPurchaseKStream.repartition(Repartitioned.streamPartitioner(rewardsStreamPartitioner)
                .withKeySerde(stringSerde)
                .withValueSerde(purchaseSerde)
                .withName(CUSTOMER_TRANSACTIONS_TOPIC)
            );

        //Adding a state store
        var rewardsStateStoreName = "rewardsPointsStore";
        var storeSupplier = Stores.inMemoryKeyValueStore(rewardsStateStoreName);
        var storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, stringSerde, integerSerde);
        builder.addStateStore(storeBuilder);

        var statefulRewardAccumulator = transByCustomerStream.transformValues(() -> new PurchaseRewardTransformer(rewardsStateStoreName), rewardsStateStoreName);

        statefulRewardAccumulator.print(Printed.<String, RewardAccumulator>toSysOut().withLabel("rewards"));

        statefulRewardAccumulator.to(REWARDS_TOPIC_SINK,
            Produced.with(stringSerde, rewardAccumulatorSerde));

        //4th PROCESSOR: BRANCH

        @SuppressWarnings("unchecked")
        KStream<String, Purchase>[] kstreamByDept = maskedPurchaseKStream.branch(this.isAmusement(), this.isElectronics());

        int amusementIndex = 0;
        int electronicsIndex = 1;

        kstreamByDept[amusementIndex].to(AMUSEMENT_TOPIC_SINK, Produced.with(stringSerde, purchaseSerde));

        kstreamByDept[electronicsIndex].to(ELECTRONICS_TOPIC_SINK, Produced.with(stringSerde, purchaseSerde));

        var purchaseRepository = new PurchaseRepositorySysOut();

        ForeachAction<String, Purchase> purchaseForeachAction = (key, purchase) ->
            purchaseRepository.save(purchase);

        maskedPurchaseKStream
            .filter((key, purchase) -> EMPLOYEE_ID.equals(purchase.getEmployeeId()))
            .foreach(purchaseForeachAction);

        return builder.build();
    }

    private Predicate<String, Purchase> isElectronics() {
        return (key, purchase) -> {
            boolean is;
            if (purchase == null || purchase.getDepartment() == null) {
                is = false;
            } else {
                is = purchase.getDepartment().equalsIgnoreCase("electronics") ||
                    purchase.getDepartment().equalsIgnoreCase("computers");
            }
            return is;
        };
    }

    private Predicate<String, Purchase> isAmusement() {
        return (key, purchase) -> {
            boolean is;
            if (purchase == null || purchase.getDepartment() == null) {
                is = false;
            } else {
                is = purchase.getDepartment().equalsIgnoreCase("books") ||
                    purchase.getDepartment().equalsIgnoreCase("movies") ||
                    purchase.getDepartment().equalsIgnoreCase("music") ||
                    purchase.getDepartment().equalsIgnoreCase("games") ||
                    purchase.getDepartment().equalsIgnoreCase("toys");
            }
            return is;
        };
    }
}

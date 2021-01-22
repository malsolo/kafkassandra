# Kafka Streams in action

Let's follow the manning book, package purchase.

## Purchase topology

From part 2, kafka streams development, chapters 3 and 4: developing kafka streams and streams and state.

### Kafka Streams DSL: create the application.

Before constructing the topology we need the data model and the expected *deser* classes:

Packages model and serde.

### Kafka Streams DSL: create the topics.

```
bin/kafka-topics --delete --bootstrap-server localhost:9092 --topic ksia-transactions
bin/kafka-topics --delete --bootstrap-server localhost:9092 --topic ksia-purchases
bin/kafka-topics --delete --bootstrap-server localhost:9092 --topic ksia-patterns
bin/kafka-topics --delete --bootstrap-server localhost:9092 --topic ksia-rewards
bin/kafka-topics --delete --bootstrap-server localhost:9092 --topic ksia-amusement
bin/kafka-topics --delete --bootstrap-server localhost:9092 --topic ksia-electronics
bin/kafka-topics --delete --bootstrap-server localhost:9092 --topic ksia-correlated-purchases
bin/kafka-topics --delete --bootstrap-server localhost:9092 --topic ksia-customer-transactions

bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-transactions
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-purchases
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic ksia-patterns
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic ksia-rewards
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-amusement
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-electronics
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-correlated-purchases
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-customer-transactions

bin/kafka-topics --list --bootstrap-server localhost:9092 | grep ksia-
```

### Kafka Consumer: verify consumer groups

Find out the consumer groups

```
$ bin/kafka-consumer-groups --bootstrap-server localhost:9092 --list | grep KSIA
KSIA_CORRELATED_PURCHASES_GROUP
KSIA_AMUSEMENTS_GROUP
KSIA_REWARDS_GROUP
KSIA_ELECTRONICS_GROUP
KSIA_PATTERNS_GROUP
KSIA_PURCHASES_GROUP
```

Describe each consumer group

```
$ bin/kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group KSIA_CORRELATED_PURCHASES_GROUP

GROUP                           TOPIC                     PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                                       HOST            CLIENT-ID
KSIA_CORRELATED_PURCHASES_GROUP ksia-correlated-purchases 0          40              40              0               correlatedPurchasesConsumer1-646c925a-004b-4a7d-ae25-c3cb89fc35df /172.19.0.1     correlatedPurchasesConsumer1

$ bin/kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group KSIA_AMUSEMENTS_GROUP

GROUP                 TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                             HOST            CLIENT-ID
KSIA_AMUSEMENTS_GROUP ksia-amusement  0          0               0               0               amusementConsumer1-41b8be8c-3c7b-4bfc-ae9f-49c23d1f5713 /172.19.0.1     amusementConsumer1

$ bin/kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group KSIA_REWARDS_GROUP

GROUP              TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                                     HOST            CLIENT-ID
KSIA_REWARDS_GROUP ksia-rewards    0          6               6               0               rewardAccumulatorConsumer1-8113f53f-9454-4569-9a4c-a805175f74cf /172.19.0.1     rewardAccumulatorConsumer1
KSIA_REWARDS_GROUP ksia-rewards    2          104             104             0               rewardAccumulatorConsumer3-f21e0a76-503f-4c54-8ab8-77e3311f01af /172.19.0.1     rewardAccumulatorConsumer3
KSIA_REWARDS_GROUP ksia-rewards    1          79              79              0               rewardAccumulatorConsumer2-641c6764-072c-4a3d-bf25-b18cc7a7fa06 /172.19.0.1     rewardAccumulatorConsumer2


$ bin/kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group KSIA_ELECTRONICS_GROUP

GROUP                  TOPIC            PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                               HOST            CLIENT-ID
KSIA_ELECTRONICS_GROUP ksia-electronics 0          0               0               0               electronicsConsumer1-ca468ea8-dcb0-4678-870a-c7af4d18f96e /172.19.0.1     electronicsConsumer1


$ bin/kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group KSIA_PATTERNS_GROUP

GROUP               TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                                   HOST            CLIENT-ID
KSIA_PATTERNS_GROUP ksia-patterns   1          144             144             0               purchasePatternConsumer2-70399183-acb6-417b-8c71-6b32d7a66f16 /172.19.0.1     purchasePatternConsumer2
KSIA_PATTERNS_GROUP ksia-patterns   0          62              62              0               purchasePatternConsumer1-730490f8-d5ed-492c-a74d-fa5d676d0feb /172.19.0.1     purchasePatternConsumer1

$ bin/kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group KSIA_PURCHASES_GROUP

GROUP                TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                            HOST            CLIENT-ID
KSIA_PURCHASES_GROUP ksia-purchases  0          109             109             0               purchaseConsumer1-4332b611-3f04-4e1f-9d8d-f4652afc3b16 /172.19.0.1     purchaseConsumer1

```
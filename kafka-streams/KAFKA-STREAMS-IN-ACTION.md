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
bin/kafka-topics --delete --bootstrap-server localhost:9092 --topic ksia-customer-transactions

bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-transactions
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-purchases
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic ksia-patterns
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic ksia-rewards
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-amusement
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-electronics
bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ksia-customer-transactions

bin/kafka-topics --list --bootstrap-server localhost:9092 | grep ksia-
```
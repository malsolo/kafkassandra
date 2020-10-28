# Kafka Streams

See https://docs.confluent.io/current/streams/index.html

## Kafka Streams Quick Start

See https://docs.confluent.io/current/streams/quickstart.html#streams-quickstart

```
$ echo -e "all streams lead to kafka\nhello kafka streams\njoin kafka summit" > /tmp/file-input.txt

$ cat /tmp/file-input.txt | ./bin/kafka-console-producer --broker-list localhost:9092 --topic streams-wordcount-input

$ ./bin/kafka-console-consumer --bootstrap-server localhost:9092 \
          --topic streams-wordcount-output \
          --from-beginning \
          --formatter kafka.tools.DefaultMessageFormatter \
          --property print.key=true \
          --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
          --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```

# Streams Developer Guide

See https://docs.confluent.io/current/streams/developer-guide/index.html

## Writing a Streams Application

See https://docs.confluent.io/current/streams/developer-guide/write-streams.html

## Testing Streams Code

See https://docs.confluent.io/current/streams/developer-guide/test-streams.html
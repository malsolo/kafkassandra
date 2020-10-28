package com.malsolo.kafkassandra.kafka.streams;

import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

public class WordCountApplication {

    static final String STREAMS_WORDCOUNT_INPUT = "streams-wordcount-input";
    public static final String STREAMS_WORDCOUNT_OUTPUT = "streams-wordcount-output";
    public static final String STREAMS_WORDCOUNT_STORE = "streams-wordcount-store";

    public Topology createTopology() {
        // Serializers/deserializers (serde) for String and Long types. Just in case the default doesn't fit our needs.
        var stringSerde = Serdes.String();
        var longSerde = Serdes.Long();

        var builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream(
            STREAMS_WORDCOUNT_INPUT,
            Consumed.with(stringSerde, stringSerde));

        KTable<String, Long> wordCounts = textLines
            .flatMapValues(texLine -> Arrays.asList(texLine.toLowerCase().split("\\W+")))
            .groupBy((key, word) -> word)
            .count(Materialized.as(STREAMS_WORDCOUNT_STORE));

        wordCounts.toStream().to(STREAMS_WORDCOUNT_OUTPUT, Produced.with(stringSerde, longSerde));

        return builder.build();
    }

    public static void main(String[] args) {
        var props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        var wordAccountApp = new WordCountApplication();

        var topology = wordAccountApp.createTopology();

        var streams = new KafkaStreams(topology, props);
        streams.start();

        System.out.println(topology.describe());

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

}

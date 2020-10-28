package com.malsolo.kafkassandra.kafka.streams.boot;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KafkaStreamsBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsBootApplication.class, args);
	}

	@Bean
	public Consumer<KStream<String, String>> process() {
		return input -> input.foreach((key, value) ->
			System.out.printf("Key: %s - Value: %s %n", key, value)
		);
	}

	@Bean
	public Function<KStream<Object, String>, KStream<String, WordCount>> wordCount() {
		return input -> input
			.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
			.map((key, value) -> new KeyValue<>(value, value))
			.groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
			.windowedBy(TimeWindows.of(Duration.ofMillis(5000)))
			.count(Materialized.as("wordcount-store"))
			.toStream()
			.map((key, value) -> new KeyValue<>(key.key(), new WordCount(key.key(), value,
				fromMillis(key.window().start()), fromMillis(key.window().end()))));
	}

	private LocalDateTime fromMillis(long millis) {
		return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}

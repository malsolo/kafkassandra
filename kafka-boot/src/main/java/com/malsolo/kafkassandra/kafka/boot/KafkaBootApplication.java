package com.malsolo.kafkassandra.kafka.boot;

import java.util.stream.Stream;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KafkaBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaBootApplication.class, args);
	}

	@Bean
	public ApplicationRunner produceMessages(KafkaBootProducer kafkaBootProducer, KafkaProperties kafkaProperties) {
		return args -> {
			if (kafkaProperties.getProducer().isAutoStart()) {
				Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
					.forEach(i -> kafkaBootProducer.sendMessage(i, "Message " + i));
			}
		};
	}

}

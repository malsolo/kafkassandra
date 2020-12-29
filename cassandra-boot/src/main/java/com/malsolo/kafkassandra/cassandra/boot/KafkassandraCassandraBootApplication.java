package com.malsolo.kafkassandra.cassandra.boot;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(AcmeProperties.class)
public class KafkassandraCassandraBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkassandraCassandraBootApplication.class, args);
    }

    @Bean
    public ApplicationRunner testTypeSafeConfiguration(AcmeProperties acmeProperties) {
		return args -> System.out.printf("ACME. Enabled %b for user %s (pwd: %s)\n",
            acmeProperties.isEnabled(),
            acmeProperties.getSecurity().getUsername(),
            acmeProperties.getSecurity().getPassword()
        );
    }


}

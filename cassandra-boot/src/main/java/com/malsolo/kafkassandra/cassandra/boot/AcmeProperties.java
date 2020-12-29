package com.malsolo.kafkassandra.cassandra.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("acme")
@Data
public class AcmeProperties {

    private boolean enabled;
    private final Security security = new Security();

    @Data
    public static class Security {
        private String username;
        private String password;
    }

}

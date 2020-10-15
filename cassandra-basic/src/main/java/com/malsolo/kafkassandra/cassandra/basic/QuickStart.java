package com.malsolo.kafkassandra.cassandra.basic;

import com.datastax.oss.driver.api.core.CqlSession;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickStart {

    private static final Logger logger = LoggerFactory.getLogger(QuickStart.class);

    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) {
        try (var session = CqlSession.builder().build()) {
            var rs = session.execute("select release_version from system.local");
            var row = rs.one();
            logger.info("\n{}\n{}\n{}\n", "RELEASE VERSION", "---------------",
                Optional.ofNullable(row.getString("release_version")).orElse("No version"));
        }

    }


}

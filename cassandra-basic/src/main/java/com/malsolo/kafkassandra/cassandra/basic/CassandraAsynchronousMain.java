package com.malsolo.kafkassandra.cassandra.basic;

import com.datastax.oss.driver.api.core.CqlSession;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraAsynchronousMain {

    private static final Logger logger = LoggerFactory.getLogger(CassandraAsynchronousMain.class);

    public static void main(String[] args) {
        var sessionStage = CqlSession.builder().buildAsync();

        sessionStage.thenAccept(session -> System.out.println(Thread.currentThread().getName()));

        var releaseVersionResponseStage = sessionStage.thenCompose(
            session ->
                session.executeAsync("SELECT release_version FROM system.local")
        );

        releaseVersionResponseStage.thenAccept(resultSet -> System.out.println(Thread.currentThread().getName()));

        @SuppressWarnings("ConstantConditions")
        var versionStage = releaseVersionResponseStage.thenApply(
            resultSet ->
                Optional.ofNullable(resultSet.one().getString("release_version")).orElse("no version")
        );

        versionStage.whenComplete(
            (version, throwable) -> {
                logger.info("\nVERSION\n{}\n", throwable == null ? version : throwable.getMessage());
                sessionStage.thenAccept(CqlSession::closeAsync);
            }
        );

        logger.info("Let's wait");
        //Thread.sleep(1000);
    }

}

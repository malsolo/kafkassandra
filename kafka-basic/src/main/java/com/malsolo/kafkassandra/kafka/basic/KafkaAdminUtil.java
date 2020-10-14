package com.malsolo.kafkassandra.kafka.basic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaAdminUtil {

    private static final Logger logger = LoggerFactory.getLogger(KafkaAdminUtil.class);

    public static void createTopic(final String topic, final int partitions, final int replication, final Properties cloudConfig) {
        final NewTopic newTopic = new NewTopic(topic, partitions, (short) replication);
        try (final AdminClient adminClient = AdminClient.create(cloudConfig)) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (final InterruptedException | ExecutionException e) {
            // Ignore if TopicExistsException, which may be valid if topic exists
            if ((e.getCause() instanceof TopicExistsException)) {
                logger.warn("Topic already exists: {}", e.getMessage());
            }
            else {
                throw new RuntimeException(e);
            }
        }
    }

    public static Properties loadConfig(final String configFile) throws IOException {
        var path = Paths.get(configFile);
        if (!Files.exists(path)) {
            throw new IOException(configFile + " not found: " + path.toFile().getAbsolutePath());
        }
        final Properties cfg = new Properties();
        try (InputStream inputStream = new FileInputStream(configFile)) {
            cfg.load(inputStream);
        }
        return cfg;
    }


}

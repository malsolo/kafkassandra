package com.malsolo.kafkassandra.kafka.basic;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test() {
        logger.warn(">>>>> LOG <<<<<<");
    }
}

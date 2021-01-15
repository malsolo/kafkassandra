package com.malsolo.kafkassandra.kafka.streams.purchase.config;

public class TopicsConfig {
    public static final String PURCHASE_APPLICATION_ID = "purchase-application";
    public static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static final String TRANSACTIONS_TOPIC_SOURCE = "transactions";
    public static final String PURCHASES_TOPIC_SINK = "purchases";
    public static final String PATTERNS_TOPIC_SINK = "patterns";
    public static final String REWARDS_TOPIC_SINK = "rewards";

    public static final String CUSTOMER_TRANSACTIONS_TOPIC = "customer_transactions";

    public static final String AMUSEMENT_TOPIC_SINK = "amusement";
    public static final String ELECTRONICS_TOPIC_SINK = "electronics";
    public static final String EMPLOYEE_ID = "000000";
}

package com.malsolo.kafkassandra.kafka.streams.purchase.config;

public class TopicsConfig {
    public static final String PURCHASE_APPLICATION_ID = "purchase-application";
    public static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static final String TRANSACTIONS_TOPIC_SOURCE = "ksia-transactions";
    public static final String PURCHASES_TOPIC_SINK = "ksia-purchases";
    public static final String PATTERNS_TOPIC_SINK = "ksia-patterns";
    public static final String REWARDS_TOPIC_SINK = "ksia-rewards";

    public static final String CUSTOMER_TRANSACTIONS_TOPIC = "ksia-customer-transactions";

    public static final String AMUSEMENT_TOPIC_SINK = "ksia-amusement";
    public static final String ELECTRONICS_TOPIC_SINK = "ksia-electronics";
    public static final String EMPLOYEE_ID = "000000";
}

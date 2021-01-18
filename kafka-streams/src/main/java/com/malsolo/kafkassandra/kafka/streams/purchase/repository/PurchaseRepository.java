package com.malsolo.kafkassandra.kafka.streams.purchase.repository;


import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;

public interface PurchaseRepository {
    void save(Purchase purchase);
}

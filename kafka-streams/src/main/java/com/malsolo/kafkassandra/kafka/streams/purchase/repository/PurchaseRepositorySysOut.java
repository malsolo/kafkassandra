package com.malsolo.kafkassandra.kafka.streams.purchase.repository;


import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;

public class PurchaseRepositorySysOut implements PurchaseRepository {
    @Override
    public void save(Purchase purchase) {
        System.out.printf("Saving transaction on %tB %<te,  %<tY  %<tT %<Tp for %s, item: %s\n",
                purchase.getPurchaseDate(),
                purchase.getEmployeeId(),
                purchase.getItemPurchased());
    }
}

package com.malsolo.kafkassandra.kafka.streams.purchase.model;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrelatedPurchase {

    private String customerId;
    private List<String> itemsPurchased;
    private double totalAmount;
    private Date firstPurchaseTime;
    private Date secondPurchaseTime;

}

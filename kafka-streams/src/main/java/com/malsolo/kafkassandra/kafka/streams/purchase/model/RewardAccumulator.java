package com.malsolo.kafkassandra.kafka.streams.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardAccumulator {

    private String customerId;
    private double purchaseTotal;
    private int currentRewardPoints;

    private int totalRewardPoints;
    private int daysFromLastPurchase;


    public static RewardAccumulator fromPurchase(Purchase purchase) {
        var rewardAccumulator = new RewardAccumulator();

        rewardAccumulator.setCustomerId(purchase.getLastName()+","+purchase.getFirstName());
        rewardAccumulator.setPurchaseTotal(purchase.getPrice() * (double) purchase.getQuantity());
        rewardAccumulator.setCurrentRewardPoints((int) rewardAccumulator.getPurchaseTotal());

        return rewardAccumulator;
    }

    public void addRewardPoints(int previousTotalPoints) {
        this.totalRewardPoints += previousTotalPoints;
    }

}

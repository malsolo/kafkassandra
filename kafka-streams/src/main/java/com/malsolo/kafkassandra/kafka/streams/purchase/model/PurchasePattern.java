package com.malsolo.kafkassandra.kafka.streams.purchase.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasePattern {

    private String zipCode;
    private String item;
    private Date date;
    private double amount;

    public static PurchasePattern fromPurchase(Purchase purchase) {
        return new PurchasePattern(
            purchase.getZipCode(),
            purchase.getItemPurchased(),
            purchase.getPurchaseDate(),
            purchase.getPrice() * purchase.getQuantity()
        );
    }

}

package com.malsolo.kafkassandra.kafka.streams.purchase.model;

import java.util.Date;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {

    private static final String CC_NUMBER_REPLACEMENT="xxxx-xxxx-xxxx-";

    private String firstName;
    private String lastName;
    private String customerId;
    private String creditCardNumber;
    private String itemPurchased;
    private String department;
    private String employeeId;
    private int quantity;
    private double price;
    private Date purchaseDate;
    private String zipCode;
    private String storeId;

    public Purchase maskCreditCard() {
        Objects.requireNonNull(this.creditCardNumber, "Credit Card can't be null");
        String[] parts = this.creditCardNumber.split("-");
        if (parts.length < 4 ) {
            this.creditCardNumber = "xxxx";
        } else {
            String last4Digits = this.creditCardNumber.split("-")[3];
            this.creditCardNumber = CC_NUMBER_REPLACEMENT + last4Digits;
        }
        return this;
    }


}

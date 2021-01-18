package com.malsolo.kafkassandra.kafka.streams.purchase.clients.datagen;

import com.github.javafaker.Faker;
import com.github.javafaker.Finance;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class Generator {

    private static Faker dateFaker = new Faker();
    private static Supplier<Date> timestampGenerator = () -> dateFaker.date().past(15, TimeUnit.MINUTES, new Date());

    public static Purchase generatePurchase() {
        Faker faker = new Faker();

        String firstName = faker.name().firstName();
        String lastName = faker.name().firstName();
        String customerId = faker.idNumber().valid();
        String creditCardNumber = generateCreditCardNumbers();
        String itemPurchased = faker.commerce().productName();
        String department = faker.commerce().department();
        String employeeId = Long.toString(faker.number().randomNumber(5, false));
        int quantity = faker.number().numberBetween(1, 5);
        double price = Double.parseDouble(faker.commerce().price(4.00, 295.00));
        Date purchaseDate = timestampGenerator.get();
        String zipCode = faker.options().option("47197-9482", "97666", "113469", "334457");
        String storeId = Long.toString(faker.number().randomNumber(6, true));

        return new Purchase(firstName, lastName, customerId, creditCardNumber,
            itemPurchased, department, employeeId, quantity, price, purchaseDate, zipCode, storeId);
    }

    private static String generateCreditCardNumbers() {
        Pattern visaMasterCardAmex = Pattern.compile("(\\d{4}-){3}\\d{4}");
        Finance finance = new Faker().finance();
        String cardNumber = finance.creditCard();
        while (!visaMasterCardAmex.matcher(cardNumber).matches()) {
            cardNumber = finance.creditCard();
        }
        return cardNumber;
    }


}

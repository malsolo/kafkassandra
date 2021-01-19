package com.malsolo.kafkassandra.kafka.streams.purchase.clients.datagen;

import static com.malsolo.kafkassandra.kafka.streams.purchase.config.TopicsConfig.EMPLOYEE_ID;

import com.github.javafaker.Faker;
import com.malsolo.kafkassandra.kafka.streams.purchase.model.Purchase;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class Generator {

    public static final String ELECTRONICS_DPT = "electronics";
    public static final String BOOKS_DPT = "books";

    private static Faker dateFaker = new Faker();
    private static Faker faker = new Faker();
    private static Random random = new Random();

    public static String CUSTOMER_ID_FOR_JOINING_PURCHASES = faker.idNumber().valid();

    private static Supplier<Date> timestampGenerator = () -> dateFaker.date().past(15, TimeUnit.MINUTES, new Date());

    public static Purchase generatePurchase() {

        var customerId = faker.idNumber().valid();
        var firstName = faker.name().firstName();
        var lastName = faker.name().firstName();
        var creditCardNumber = generateCreditCardNumbers();
        var itemPurchased = faker.commerce().productName();
        String department;
        var oneThird = random.nextInt(3);
        switch (oneThird) {
            case 0:
                department = ELECTRONICS_DPT;
                break;
            case 1:
                department = BOOKS_DPT;
                break;
            default:
                department = faker.commerce().department();
                break;
        }
        String employeeId;
        if (random.nextBoolean()) {
            employeeId = Long.toString(faker.number().randomNumber(5, false));
        }
        else {
            employeeId = EMPLOYEE_ID;
        }
        var quantity = faker.number().numberBetween(1, 5);
        var price = Double.parseDouble(faker.commerce().price(4.00, 295.00));
        var purchaseDate = timestampGenerator.get();
        var zipCode = faker.options().option("47197-9482", "97666", "113469", "334457");
        var storeId = Long.toString(faker.number().randomNumber(6, true));

        return new Purchase(firstName, lastName, customerId, creditCardNumber,
            itemPurchased, department, employeeId, quantity, price, purchaseDate, zipCode, storeId);
    }

    private static String generateCreditCardNumbers() {
        var visaMasterCardAmex = Pattern.compile("(\\d{4}-){3}\\d{4}");
        var finance = new Faker().finance();
        var cardNumber = finance.creditCard();
        while (!visaMasterCardAmex.matcher(cardNumber).matches()) {
            cardNumber = finance.creditCard();
        }
        return cardNumber;
    }


}

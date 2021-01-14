package com.malsolo.avro.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vavr.API;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserTest {

    public static final String USERS_AVRO_PATH = "target/users.avro";
    Logger logger = LoggerFactory.getLogger(UserTest.class);

    @Test
    public void testSerializing() throws IOException {
        logger.info("Test serializing");
        var users = createUsers();

        assertNotNull(users);
        assertTrue(users.size() > 0);

        var schema = users.get(0).getSchema();

        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter);
        dataFileWriter.create(schema, new File(USERS_AVRO_PATH));

        users.stream()
            .map(API.unchecked(user -> appendToDataFileWriter(dataFileWriter, user)))
            .forEach(System.out::println);

        dataFileWriter.close();

        assertTrue(Files.exists(Paths.get(USERS_AVRO_PATH)));
    }

    private User appendToDataFileWriter(DataFileWriter<User> dataFileWriter, User user) throws IOException {
        dataFileWriter.append(user);
        return user;
    }

    private List<User> createUsers() {
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        // Leave favorite color null

        // Alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Construct via builder
        User user3 = User.newBuilder()
            .setName("Charlie")
            .setFavoriteColor("blue")
            .setFavoriteNumber(null)
            .build();

        return Arrays.asList(user1, user2, user3);
    }

}

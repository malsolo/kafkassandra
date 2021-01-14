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
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserTest {

    public static final String USERS_AVRO_PATH = "target/users.avro";
    Logger logger = LoggerFactory.getLogger(UserTest.class);

    @Test
    @Order(1)
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
            .forEach(user -> logger.info("User serialized: {}", user));

        dataFileWriter.close();

        assertTrue(Files.exists(Paths.get(USERS_AVRO_PATH)));
    }

    @Test
    @Order(2)
    public void testDeserializing() throws IOException {
        logger.info("Test deserializing");
        var users = createUsers();

        // Deserialize Users from disk
        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<>(new File(USERS_AVRO_PATH), userDatumReader);
        User user = null;
        int i = 0;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            user = dataFileReader.next(user);
            Assertions.assertEquals(users.get(i), user);
            i++;
            logger.info("User deserialized: {}", user);
        }
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

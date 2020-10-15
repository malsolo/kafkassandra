package com.malsolo.kafkassandra.cassandra.basic;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.deleteFrom;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import java.net.InetSocketAddress;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CassandraQuickStart {

    private static final Logger logger = LoggerFactory.getLogger(CassandraQuickStart.class);
    public static final String CONTACT_POINT_1_HOST = "127.0.0.1";
    public static final int CONTACT_POINT_1_PORT = 9042;
    public static final String KEYSPACE_NAME = "demo";
    public static final String DATACENTER = "datacenter1";
    public static final String USERS_TABLE = "users";

    @SuppressWarnings("ConstantConditions")
    /*
     * Create the keyspace and table
     * cqlsh> DESC keyspaces;
     *
     * cqlsh> CREATE KEYSPACE demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
     *
     * cqlsh>
     CREATE TABLE demo.users (
        lastname text PRIMARY KEY,
        age int,
        city text,
        email text,
        firstname text);
     */
    public static void main(String[] args) {
        try (var session = CqlSession.builder()
            .addContactPoint(new InetSocketAddress(CONTACT_POINT_1_HOST, CONTACT_POINT_1_PORT))
            .withKeyspace(KEYSPACE_NAME)
            .withLocalDatacenter(DATACENTER)
            .build()) {
            var rs = session.execute("select release_version from system.local");
            var row = rs.one();
            logger.info("\n{}\n{}\n{}\n", "RELEASE VERSION", "---------------",
                Optional.ofNullable(row.getString("release_version")).orElse("No version"));

            setUser(session, "Jones", 35, "Austin", "bob@example.com", "Bob");

            getUser(session, "Jones");

            updateUser(session, 36, "Jones");

            getUser(session, "Jones");

            deleteUser(session, "Jones");
        }
    }

    private static void setUser(CqlSession session, String lastname, int age, String city, String email,
        String firstname) {
        // https://docs.datastax.com/en/developer/java-driver/4.9/manual/query_builder/#query-builder
        session.execute(
            SimpleStatement.builder("INSERT INTO " + USERS_TABLE + " (lastname, age, city, email, firstname) VALUES (?,?,?,?,?)")
                .addPositionalValues(lastname, age, city, email, firstname)
                .build());

        logger.info("USER CREATED {} {}", firstname, age);
    }

    @SuppressWarnings("ConstantConditions")
    private static void getUser(CqlSession session, String lastname) {
        // https://docs.datastax.com/en/developer/java-driver/4.9/manual/query_builder/#immutability
        var selectUsers = "SELECT * FROM " + USERS_TABLE + " WHERE lastname=?";
        var preparedSelectUser = session.prepare(selectUsers);
        var rs = session.execute(preparedSelectUser.bind(lastname));

        var row = rs.one();

        logger.info("USER OBTAINED {} {}", row.getString("firstname"), row.getInt("age"));
    }


    private static void updateUser(CqlSession session, int age, String lastname) {
        var updateUser = update(USERS_TABLE)
            .setColumn("age", bindMarker())
            .whereColumn("lastname")
            .isEqualTo(bindMarker());

        var prepareUpdateUser = session.prepare(updateUser.build());

        session.execute(prepareUpdateUser.bind(age, lastname));

        logger.info("USER UPDATED {} {}", lastname, age);
    }

    private static void deleteUser(CqlSession session, String lastname) {
        var deleteUser = deleteFrom(USERS_TABLE)
            .whereColumn("lastname")
            .isEqualTo(bindMarker())
            .build();

        var preparedDeleteUser = session.prepare(deleteUser);

        session.execute(preparedDeleteUser.bind(lastname));

        logger.info("USER DELETED {}", lastname);
    }

}

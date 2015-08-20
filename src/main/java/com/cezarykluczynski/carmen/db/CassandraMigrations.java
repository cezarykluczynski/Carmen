package com.cezarykluczynski.carmen.db;

import com.netflix.astyanax.*;
import com.netflix.astyanax.impl.*;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.CqlResult;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.serializers.UUIDSerializer;
import com.netflix.astyanax.connectionpool.*;
import com.netflix.astyanax.connectionpool.impl.*;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.util.RangeBuilder;
import com.netflix.astyanax.thrift.model.ThriftRowImpl;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnList;

import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.Iterator;

class CassandraMigrations {

    public static void main(String[] args) throws ConnectionException, IOException {
        System.out.println("Cassandra migrations started.");

        String contactpoints = null;
        Integer port = null;
        String cluster = null;
        String keyspaceName = null;
        String version = null;

        try {
            Properties properties = new Properties();
            InputStream inputStream = CassandraMigrations.class.getClassLoader().getResourceAsStream("cassandra.properties");
            properties.load(inputStream);

            contactpoints = properties.getProperty("cassandra.contactpoints");
            port = Integer.parseInt(properties.getProperty("cassandra.thrift_port"));
            cluster = properties.getProperty("cassandra.cluster");
            keyspaceName = properties.getProperty("cassandra.keyspace");
            version = (String) properties.getProperty("cassandra.version");
        } catch (IOException ioe) {
            System.out.println("Carmen: cassandra.properties not found or incomplete.");

            throw ioe;
        }

        AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
            .forCluster(cluster)
            .forKeyspace(keyspaceName)
            .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
                .setTargetCassandraVersion(version)
                .setCqlVersion("3.1.1")
                .setDiscoveryType(NodeDiscoveryType.NONE)
            )
            .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
                .setPort(port)
                .setMaxConnsPerHost(1)
                .setSeeds(contactpoints + ":" + Integer.toString(port)) // TODO: allow multiple contactpoints
            )
            .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
            .buildKeyspace(ThriftFamilyFactory.getInstance());

        context.start();
        Keyspace keyspace = context.getClient();

        createKeyspace(keyspace);
        createMigrationsTable(keyspace);
        migrateFollowersAndFollowees(keyspace);
        migrateFollowingBeingFollowees(keyspace);

        context.shutdown();
        System.exit(0);
    }

    private static void createKeyspace(Keyspace keyspace) throws ConnectionException {
        String keyspaceName = keyspace.getKeyspaceName();

        try {
            keyspace.describeKeyspace();

            System.out.println("Carmen: Keyspace \"" + keyspaceName + "\" already exists.");
        } catch(Exception e) {
            try {
                 keyspace.createKeyspace(ImmutableMap.<String, Object>builder()
                    .put("strategy_options", ImmutableMap.<String, Object>builder()
                            .put("replication_factor", "2")
                            .build())
                    .put("strategy_class", "SimpleStrategy")
                    .build()
                );
                System.out.println("Carmen: Keyspace \"" + keyspaceName + "\" created.");
            } catch (ConnectionException ce) {
                System.err.println("Carmen: Could not create keyspace \"" + keyspaceName + "\".");
                throw ce;
            }
        }
    }

    private static void createMigrationsTable(Keyspace keyspace) throws ConnectionException {
        String keyspaceName = keyspace.getKeyspaceName();

        ColumnFamily<UUID, String> SCHEMA_MIGRATIONS = ColumnFamily.newColumnFamily(
            "schema_migrations",
            UUIDSerializer.get(),
            StringSerializer.get()
        );

        OperationResult<CqlResult<UUID, String>> result
            = keyspace.prepareQuery(SCHEMA_MIGRATIONS)
                .withCql("CREATE TABLE IF NOT EXISTS " + keyspaceName  +".schema_migrations " +
                    "(version int, name varchar, created timestamp, PRIMARY KEY(version));")
                .execute();
    }

    private static void migrateFollowersAndFollowees(Keyspace keyspace) throws ConnectionException {
        String keyspaceName = keyspace.getKeyspaceName();

        ColumnFamily<UUID, String> SCHEMA_MIGRATIONS = ColumnFamily.newColumnFamily(
            "followers_and_followees",
            UUIDSerializer.get(),
            StringSerializer.get()
        );

        OperationResult<CqlResult<UUID, String>> result
            = keyspace.prepareQuery(SCHEMA_MIGRATIONS)
                .withCql("CREATE TABLE IF NOT EXISTS " + keyspaceName  +".followers_and_followees " +
                    "(id uuid, user_id int, followers_count int, followees_count int, shared_count int, PRIMARY key(id));")
                .execute();
    }

    private static void migrateFollowingBeingFollowees(Keyspace keyspace) throws ConnectionException {
    	String keyspaceName = keyspace.getKeyspaceName();

        ColumnFamily<UUID, String> SCHEMA_MIGRATIONS = ColumnFamily.newColumnFamily(
            "followeers_being_followees",
            UUIDSerializer.get(),
            StringSerializer.get()
        );

        OperationResult<CqlResult<UUID, String>> result
        = keyspace.prepareQuery(SCHEMA_MIGRATIONS)
            .withCql("CREATE TABLE IF NOT EXISTS " + keyspaceName  +".followers_being_followees " +
                "(id uuid, user_id int, follower_being_followee int, PRIMARY key(id));")
            .execute();
    }

}
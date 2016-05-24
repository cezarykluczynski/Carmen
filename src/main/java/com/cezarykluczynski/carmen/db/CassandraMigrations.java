package com.cezarykluczynski.carmen.db;

import com.cezarykluczynski.carmen.db.migration.cassandra.KeyspaceDefinition;
import com.cezarykluczynski.carmen.db.migration.cassandra.carmen.CarmenKeyspaceDefinition;
import com.contrastsecurity.cassandra.migration.CassandraMigration;
import com.netflix.astyanax.*;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.impl.*;
import com.netflix.astyanax.connectionpool.*;
import com.netflix.astyanax.connectionpool.impl.*;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
class CassandraMigrations {

    private static String contactpoints;
    private static Integer thriftPort;
    private static Integer port;
    private static String cluster;
    private static String version;

    public static void main(String[] args) throws ConnectionException, IOException {
        log.info("Carmen: Cassandra migrations started.");

        configure();
        createAndMigrateKeyspace(new CarmenKeyspaceDefinition());
        System.exit(0);
    }

    private static void createAndMigrateKeyspace(KeyspaceDefinition keyspaceDefinition) throws ConnectionException {
        createKeyspace(keyspaceDefinition);
        migrateKeyspace(keyspaceDefinition);
    }

    private static void configure() throws IOException {
        try {
            Properties properties = new Properties();
            InputStream inputStream = CassandraMigrations.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            properties.load(inputStream);

            contactpoints = properties.getProperty("cassandra.contactpoints");
            thriftPort = Integer.parseInt(properties.getProperty("cassandra.thrift_port"));
            port = Integer.parseInt(properties.getProperty("cassandra.port"));
            cluster = properties.getProperty("cassandra.cluster");
            version = properties.getProperty("cassandra.version");
        } catch (IOException ioe) {
            log.error("Carmen: config.properties for Cassandra not found or incomplete.");

            throw ioe;
        }
    }

    private static void createKeyspaceIfNotExists(Keyspace keyspace, KeyspaceDefinition keyspaceDefinition)
            throws ConnectionException {
        String keyspaceName = keyspace.getKeyspaceName();

        try {
            keyspace.describeKeyspace();

            log.info("Carmen: Keyspace \"" + keyspaceName + "\" already exists.");
        } catch(Exception e) {
            try {
                 keyspace.createKeyspace(ImmutableMap.<String, Object>builder()
                    .put("strategy_options", ImmutableMap.<String, Object>builder()
                            .put("replication_factor", keyspaceDefinition.getReplicationFactor())
                            .build())
                    .put("strategy_class", "SimpleStrategy")
                    .build()
                );
                log.debug("Carmen: Keyspace \"" + keyspaceName + "\" created.");
            } catch (ConnectionException ce) {
                log.error("Carmen: Could not create keyspace \"" + keyspaceName + "\".");
                throw ce;
            }
        }
    }

    private static void createKeyspace(KeyspaceDefinition keyspaceDefinition) throws ConnectionException {
        AstyanaxContext<Keyspace> context = createKeyspaceContext(keyspaceDefinition);
        context.start();
        createKeyspaceIfNotExists(context.getClient(), keyspaceDefinition);
        context.shutdown();
    }

    private static AstyanaxContext<Keyspace> createKeyspaceContext(KeyspaceDefinition keyspaceDefinition) {
        return new AstyanaxContext.Builder()
                .forCluster(cluster)
                .forKeyspace(keyspaceDefinition.getName())
                .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
                        .setTargetCassandraVersion(version)
                        .setCqlVersion("3.1.1")
                        .setDiscoveryType(NodeDiscoveryType.NONE)
                )
                .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
                                .setPort(thriftPort)
                                .setMaxConnsPerHost(3)
                                .setSeeds(contactpoints + ":" + Integer.toString(thriftPort))
                        // TODO: allow multiple contactpoints
                )
                .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                .buildKeyspace(ThriftFamilyFactory.getInstance());
    }

    private static void migrateKeyspace(KeyspaceDefinition keyspaceDefinition) {
        String[] scriptsLocations = new String[keyspaceDefinition.getScriptsLocations().size()];
        keyspaceDefinition.getScriptsLocations().toArray(scriptsLocations);

        com.contrastsecurity.cassandra.migration.config.Keyspace keyspace =
                new com.contrastsecurity.cassandra.migration.config.Keyspace();
        keyspace.setName(keyspaceDefinition.getName());
        com.contrastsecurity.cassandra.migration.config.Cluster migrationCluster = keyspace.getCluster();
        migrationCluster.setContactpoints(contactpoints);
        migrationCluster.setPort(port);

        CassandraMigration cassandraMigration = new CassandraMigration();
        cassandraMigration.getConfigs().setScriptsLocations(scriptsLocations);
        cassandraMigration.setKeyspace(keyspace);
        log.info("Carmen: Keyspace \"" + keyspace.getName() + "\" migration started.");
        ScheduledExecutorService consoleOutputEnsuringThread = createConsoleOutputEnsuringThread();
        cassandraMigration.migrate();
        consoleOutputEnsuringThread.shutdown();
        log.info("Carmen: Keyspace \"" + keyspace.getName() + "\" migration finished.");
    }

    private static ScheduledExecutorService createConsoleOutputEnsuringThread() {
        Runnable consoleLogger = new Runnable() {

            private int counter;

            public void run() {
                System.out.println("Cassandra migrations in progress (" + counter + " minutes elapsed)...");
                counter++;
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(consoleLogger, 0, 60, TimeUnit.SECONDS);

        return executor;
    }

}

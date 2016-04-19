package com.cezarykluczynski.carmen.db;

import com.contrastsecurity.cassandra.migration.CassandraMigration;
import com.netflix.astyanax.*;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.impl.*;
import com.netflix.astyanax.connectionpool.*;
import com.netflix.astyanax.connectionpool.impl.*;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;

import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class CassandraMigrations {

    private static final String GITHUB_SOCIAL_STATS_KEYSPACE_NAME = "github_social_stats";

    private static String contactpoints;
    private static Integer thriftPort;
    private static Integer port;
    private static String cluster;
    private static String version;

    public static void main(String[] args) throws ConnectionException, IOException {
        System.out.println("Carmen: Cassandra migrations started.");

        configure();
        createGithubSocialStatsKeyspace();
        migrateGithubSocialStatsKeyspace();
        System.exit(0);
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
            System.out.println("Carmen: config.properties for Cassandra not found or incomplete.");

            throw ioe;
        }
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

    private static void createGithubSocialStatsKeyspace() throws ConnectionException {
        AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
                .forCluster(cluster)
                .forKeyspace(GITHUB_SOCIAL_STATS_KEYSPACE_NAME)
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

        context.start();
        Keyspace keyspace = context.getClient();

        createKeyspace(keyspace);
        context.shutdown();
    }

    private static void migrateGithubSocialStatsKeyspace() {
        String[] scriptsLocations = {
                "filesystem:src/main/java/com/cezarykluczynski/carmen/db/migration/cassandra/github_social_stats/"
        };

        com.contrastsecurity.cassandra.migration.config.Keyspace keyspace =
                new com.contrastsecurity.cassandra.migration.config.Keyspace();
        keyspace.setName(GITHUB_SOCIAL_STATS_KEYSPACE_NAME);
        com.contrastsecurity.cassandra.migration.config.Cluster migrationCluster = keyspace.getCluster();
        migrationCluster.setContactpoints(contactpoints);
        migrationCluster.setPort(port);

        CassandraMigration cassandraMigration = new CassandraMigration();
        cassandraMigration.getConfigs().setScriptsLocations(scriptsLocations);
        cassandraMigration.setKeyspace(keyspace);
        cassandraMigration.migrate();
    }

}

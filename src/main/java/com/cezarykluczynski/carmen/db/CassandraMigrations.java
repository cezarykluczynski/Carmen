package com.cezarykluczynski.carmen.db;

import com.cezarykluczynski.carmen.db.migration.cassandra.KeyspaceDefinition;
import com.cezarykluczynski.carmen.db.migration.cassandra.carmen.CarmenKeyspaceDefinition;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;
import com.datastax.driver.mapping.MappingManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
class CassandraMigrations {

    private static String contactpoints;

    private static Integer port;

    private static Session session;

    public static void main(String[] args) throws IOException {
        log.info("Carmen: Cassandra migrations started.");

        configure();
        createAndMigrateKeyspace(new CarmenKeyspaceDefinition());
        log.info("Carmen: Cassandra migrations finished.");
        System.exit(0);
    }

    private static void createAndMigrateKeyspace(KeyspaceDefinition keyspaceDefinition) {
        createKeyspace(keyspaceDefinition);
        migrateKeyspace(keyspaceDefinition);
    }

    private static void configure() throws IOException {
        try {
            Properties properties = new Properties();
            InputStream inputStream = CassandraMigrations.class.getClassLoader()
                    .getResourceAsStream("application.properties");
            properties.load(inputStream);

            contactpoints = properties.getProperty("cassandra.contactpoints");
            port = Integer.parseInt(properties.getProperty("cassandra.port"));
        } catch (IOException ioe) {
            log.error("Carmen: application.properties for Cassandra not found or incomplete.");

            throw ioe;
        }
    }

    private static void createKeyspace(KeyspaceDefinition keyspaceDefinition) {
        Cluster cluster = Cluster.builder()
                .addContactPoint(contactpoints)
                .withPort(port)
                .build();

        MappingManager mappingManager = new MappingManager(cluster.connect());
        session = mappingManager.getSession();

        try {
            session.execute("CREATE KEYSPACE " + keyspaceDefinition.getName() + " WITH REPLICATION = { 'class' : " +
                    "'SimpleStrategy', 'replication_factor' : " + keyspaceDefinition.getReplicationFactor() +
                    " } AND DURABLE_WRITES = true;");
        } catch (AlreadyExistsException existsException) {
        }
    }

    private static void migrateKeyspace(KeyspaceDefinition keyspaceDefinition) {
        String[] scriptsLocations = new String[keyspaceDefinition.getScriptsLocations().size()];
        keyspaceDefinition.getScriptsLocations().toArray(scriptsLocations);

        for (int i = 0; i < scriptsLocations.length; i++) {
            try {
                Files.walk(Paths.get(scriptsLocations[i].replace("filesystem:", "./"))).forEach(filePath -> {
                    if (Files.isRegularFile(filePath) && filePath.getFileName().toString().endsWith(".cql")) {
                        try {
                            byte[] encoded = Files.readAllBytes(filePath);
                            String command = new String(encoded, "UTF-8");
                            session.execute(command);
                        } catch (IOException e) {
                        }
                    }
                });
            } catch (IOException e) {
            }
        }
    }

}

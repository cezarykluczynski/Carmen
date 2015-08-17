package com.cezarykluczynski.carmen.db;

import com.netflix.astyanax.*;
import com.netflix.astyanax.impl.*;
import com.netflix.astyanax.thrift.*;
import com.netflix.astyanax.connectionpool.*;
import com.netflix.astyanax.connectionpool.impl.*;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import org.springframework.core.env.Environment;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Properties;

import java.util.HashMap;

class CassandraMigrations {

    public static void main(String[] args) throws ConnectionException, IOException {
        System.out.println("Cassandra migrations started.");

        String contactpoints = null;
        Integer port = null;
        String cluster = null;
        String keyspaceName = null;

        try {
            Properties properties = new Properties();
            InputStream inputStream = CassandraMigrations.class.getClassLoader().getResourceAsStream("cassandra.properties");
            properties.load(inputStream);

            contactpoints = properties.getProperty("cassandra.contactpoints");
            port = Integer.parseInt(properties.getProperty("cassandra.thrift_port"));
            cluster = properties.getProperty("cassandra.cluster");
            keyspaceName = properties.getProperty("cassandra.keyspace");

            System.out.println(contactpoints);
            System.out.println(port);
            System.out.println(cluster);
            System.out.println(keyspaceName);
        } catch (IOException ioe) {
            System.out.println("Carmen: cassandra.properties not found or incomplete.");

            throw ioe;
        }

        AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
            .forCluster(cluster)
            .forKeyspace(keyspaceName)
            .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
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

        context.shutdown();
        System.exit(0);
    }

}
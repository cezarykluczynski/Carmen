package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class V17__Create_Crons_table implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE table public.crons(" +
            "    id serial," +
            "    name varchar(32)," +
            "    server varchar(32)," +
            "    enabled boolean DEFAULT FALSE," +
            "    running boolean DEFAULT FALSE," +
            "    CONSTRAINT cron_pkey PRIMARY KEY(id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}

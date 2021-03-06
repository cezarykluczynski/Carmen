package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class V16__Create_Languages_table implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE TYPE linguist_language_type AS ENUM ('data', 'programming', 'markup', 'prose', 'none');" +
            "CREATE table public.languages(" +
            "    id serial," +
            "    name varchar(30)," +
            "    linguist_type linguist_language_type," +
            "    linguist_color varchar(7)," +
            "    linguist_parent_id integer REFERENCES public.languages(id) ON DELETE SET NULL," +
            "    CONSTRAINT languages_pkey PRIMARY KEY(id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}

package com.cezarykluczynski.carmen.cron.languages.api;

public interface CassandraBuiltFile {

    void save();

    String getPath();

    String getContents();

}

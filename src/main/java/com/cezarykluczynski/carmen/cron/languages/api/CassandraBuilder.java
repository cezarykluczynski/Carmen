package com.cezarykluczynski.carmen.cron.languages.api;

public interface CassandraBuilder {

    CassandraBuiltFile build(RefreshableTable refreshableTable);

}

package com.cezarykluczynski.carmen.model.cassandra.repositories;

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace;
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics;
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics;
import com.cezarykluczynski.carmen.model.CarmenNoSQLEntity;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

@LanguagesDiffStatistics
@LanguagesStatistics
@Keyspace("repositories")
@Table("commits")
public class Commit extends CarmenNoSQLEntity {

    @PrimaryKey
    public UUID id;

    @Column("hash")
    public String hash;

    public UUID getId() {
        return id;
    }

}
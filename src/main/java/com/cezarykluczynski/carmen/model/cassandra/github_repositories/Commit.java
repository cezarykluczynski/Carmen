package com.cezarykluczynski.carmen.model.cassandra.github_repositories;

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
@Keyspace("github_repositories")
@Table("github_commits")
public class Commit extends CarmenNoSQLEntity {

    @PrimaryKey
    private UUID id;

    @Column("commit_hash")
    public String commitHash;

    public UUID getId() {
        return id;
    }

}

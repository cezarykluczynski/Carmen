package com.cezarykluczynski.carmen.model.cassandra.github_repositories;

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace;
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics;
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics;
import com.cezarykluczynski.carmen.model.CarmenNoSQLEntity;
import lombok.Data;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

@Data
@LanguagesDiffStatistics
@LanguagesStatistics
@Keyspace("github_repositories")
@Table("github_commits")
public class Commits extends CarmenNoSQLEntity {

    private UUID id;


}

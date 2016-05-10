package com.cezarykluczynski.carmen.cron.languages.fixture.entity

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics
import com.cezarykluczynski.carmen.model.cassandra.GitDescription
import org.springframework.data.cassandra.mapping.Table

@LanguagesStatistics
@LanguagesDiffStatistics
@Keyspace("github_social_stats")
@Table("entity_two")
class EntityTwo implements GitDescription {

    private UUID id

}

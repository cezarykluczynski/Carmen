package com.cezarykluczynski.carmen.cron.languages.fixture.entity

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics
import org.springframework.data.cassandra.mapping.Table

@LanguagesStatistics
@LanguagesDiffStatistics
@Keyspace("github_social_stats")
@Table("entity_one")
class EntityOne {

    private UUID id

    private Integer language_1

    private Integer language_2

    private Integer language_3

    private Integer language_1_added

    private Integer language_1_removed

    private Integer language_2_added

    private Integer language_2_removed

    private Integer language_3_added

    private Integer language_3_removed

}

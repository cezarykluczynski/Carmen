package com.cezarykluczynski.carmen.cron.languages.fixture.entity

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics
import com.cezarykluczynski.carmen.model.cassandra.GitDescription
import com.datastax.driver.mapping.annotations.Table

@LanguagesStatistics
@LanguagesDiffStatistics
@Keyspace("github_social_stats")
@Table(name = "entity_one")
class EntityOne implements GitDescription  {

    private UUID id

    public Integer language_1

    public Integer language_2

    public Integer language_3

    public Integer language_4

    public Integer language_1_added

    public Integer language_1_removed

    public Integer language_2_added

    public Integer language_2_removed

    public Integer language_3_added

    public Integer language_3_removed

    public Integer language_4_added

    public Integer language_4_removed

}

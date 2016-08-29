package com.cezarykluczynski.carmen.cron.languages.fixture.entity

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace
import com.datastax.driver.mapping.annotations.Table

@Keyspace("github_social_stats")
@Table(keyspace = "github_social_stats", name = "entity_empty")
class EntityEmpty {
}

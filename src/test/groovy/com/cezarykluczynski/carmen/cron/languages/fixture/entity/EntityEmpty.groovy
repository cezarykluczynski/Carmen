package com.cezarykluczynski.carmen.cron.languages.fixture.entity

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace
import org.springframework.data.cassandra.mapping.Table

@Keyspace("github_social_stats")
@Table("entity_empty")
class EntityEmpty {
}

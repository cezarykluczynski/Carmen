package com.cezarykluczynski.carmen.db.migration.cassandra.github_social_stats;

import com.cezarykluczynski.carmen.db.migration.cassandra.KeyspaceDefinition;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public class GitHubSocialStatsKeyspaceDefinition implements KeyspaceDefinition {

    private String name = "github_social_stats";

    private List<String> scriptsLocations = Lists.newArrayList(
            "filesystem:src/main/java/com/cezarykluczynski/carmen/db/migration/cassandra/github_social_stats/"
    );

    private String replicationFactor = "2";

}

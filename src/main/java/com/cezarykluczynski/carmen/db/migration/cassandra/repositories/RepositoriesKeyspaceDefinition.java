package com.cezarykluczynski.carmen.db.migration.cassandra.repositories;

import com.cezarykluczynski.carmen.db.migration.cassandra.KeyspaceDefinition;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public class RepositoriesKeyspaceDefinition implements KeyspaceDefinition {

    private String name = "repositories";

    private List<String> scriptsLocations = Lists.newArrayList(
            "filesystem:src/main/java/com/cezarykluczynski/carmen/db/migration/cassandra/repositories/"
    );

    private String replicationFactor = "2";

}

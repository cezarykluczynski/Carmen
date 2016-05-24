package com.cezarykluczynski.carmen.db.migration.cassandra.carmen;

import com.cezarykluczynski.carmen.db.migration.cassandra.KeyspaceDefinition;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public class CarmenKeyspaceDefinition implements KeyspaceDefinition {

    private String name = "carmen";

    private List<String> scriptsLocations = Lists.newArrayList(
            "filesystem:src/main/java/com/cezarykluczynski/carmen/db/migration/cassandra/carmen/"
    );

    private String replicationFactor = "2";

}

package com.cezarykluczynski.carmen.db.migration.cassandra;

import java.util.List;

public interface KeyspaceDefinition {

    String getName();

    List<String> getScriptsLocations();

    String getReplicationFactor();

}

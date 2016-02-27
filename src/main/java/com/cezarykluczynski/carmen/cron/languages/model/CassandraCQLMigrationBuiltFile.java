package com.cezarykluczynski.carmen.cron.languages.model;

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;
import lombok.Getter;

public class CassandraCQLMigrationBuiltFile implements CassandraBuiltFile {

    @Getter
    private String path;

    @Getter
    private String contents;

    public CassandraCQLMigrationBuiltFile(String path, String contents) {
        this.path = path;
        this.contents = contents;
    }

}

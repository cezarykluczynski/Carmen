package com.cezarykluczynski.carmen.cron.languages.model;

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;

public class CassandraBuiltFileNullObject implements CassandraBuiltFile {

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getContents() {
        return null;
    }

    @Override
    public void save() {
    }
}

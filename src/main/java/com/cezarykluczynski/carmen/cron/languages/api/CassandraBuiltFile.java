package com.cezarykluczynski.carmen.cron.languages.api;

import java.io.FileWriter;
import java.io.IOException;

public interface CassandraBuiltFile {

    String getPath();

    String getContents();

    default void save() {
        try {
            FileWriter fileWriter = new FileWriter("./" + getPath());
            fileWriter.write(getContents());
            fileWriter.close();
        } catch (IOException e) {
        }
    }

}

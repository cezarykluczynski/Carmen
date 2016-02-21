package com.cezarykluczynski.carmen.cron.languages.builder;

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;
import com.squareup.javapoet.JavaFile;
import org.apache.commons.lang3.text.StrBuilder;

import java.io.IOException;

public class CassandraJavaPoetBuiltEntityFile implements CassandraBuiltFile {

    private JavaFile javaFile;

    private String contents;

    CassandraJavaPoetBuiltEntityFile(JavaFile javaFile) {
        this.javaFile = javaFile;
    }

    @Override
    public void save() {
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getContents() {
        StrBuilder strBuilder = new StrBuilder();

        try {
            javaFile.writeTo(strBuilder);
        } catch (IOException e) {
            return null;
        }

        return strBuilder.build();
    }
}

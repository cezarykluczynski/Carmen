package com.cezarykluczynski.carmen.cron.languages.model;

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;
import com.squareup.javapoet.JavaFile;
import org.apache.commons.lang3.text.StrBuilder;

import java.io.IOException;

public class CassandraJavaPoetBuiltEntityFile implements CassandraBuiltFile {

    private JavaFile javaFile;

    public CassandraJavaPoetBuiltEntityFile(JavaFile javaFile) {
        this.javaFile = javaFile;
    }

    @Override
    public String getPath() {
        return "src/main/java/" + javaFile.packageName.replace(".", "/") + "/" + javaFile.typeSpec.name + ".java";
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

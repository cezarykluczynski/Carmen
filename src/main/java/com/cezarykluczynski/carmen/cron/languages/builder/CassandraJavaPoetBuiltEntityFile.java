package com.cezarykluczynski.carmen.cron.languages.builder;

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;
import com.squareup.javapoet.JavaFile;
import org.apache.commons.lang3.text.StrBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class CassandraJavaPoetBuiltEntityFile implements CassandraBuiltFile {

    private JavaFile javaFile;

    CassandraJavaPoetBuiltEntityFile(JavaFile javaFile) {
        this.javaFile = javaFile;
    }

    @Override
    public void save() {
        try {
            FileWriter fileWriter = new FileWriter("./" + getPath(), true);
            fileWriter.write(getContents());
            fileWriter.close();
        } catch (IOException e) {
        }
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

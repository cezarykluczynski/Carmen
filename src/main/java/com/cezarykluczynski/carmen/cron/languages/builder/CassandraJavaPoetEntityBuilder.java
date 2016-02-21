package com.cezarykluczynski.carmen.cron.languages.builder;

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuilder;
import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;

@Component
public class CassandraJavaPoetEntityBuilder implements CassandraBuilder {

    @Override
    public CassandraBuiltFile build(RefreshableTable refreshableTable) {
        return new CassandraJavaPoetBuiltEntityFile(buildJavaFile(refreshableTable));
    }

    private JavaFile buildJavaFile(RefreshableTable refreshableTable) {
        TypeSpec.Builder typeSpecBuilder = createBuilder(refreshableTable);
        TypeSpec refreshableTableSpec = typeSpecBuilder.build();
        return createJavaFile(refreshableTable, refreshableTableSpec);
    }

    private TypeSpec.Builder createBuilder(RefreshableTable refreshableTable) {
        return TypeSpec.classBuilder(refreshableTable.getBaseClass().getSimpleName())
                .addModifiers(Modifier.PUBLIC);
    }

    private JavaFile createJavaFile(RefreshableTable refreshableTable, TypeSpec typeSpec) {
        return JavaFile.builder(refreshableTable.getBaseClass().getPackage().getName(), typeSpec)
                .skipJavaLangImports(true)
                .build();
    }

}

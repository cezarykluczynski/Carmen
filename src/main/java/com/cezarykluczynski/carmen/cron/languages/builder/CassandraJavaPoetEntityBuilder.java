package com.cezarykluczynski.carmen.cron.languages.builder;

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuilder;
import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.model.CarmenNoSQLEntity;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.*;
import lombok.Data;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Table;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;

@Component
public class CassandraJavaPoetEntityBuilder implements CassandraBuilder {

    @Override
    public CassandraBuiltFile build(RefreshableTable refreshableTable) {
        return new CassandraJavaPoetBuiltEntityFile(buildJavaFile(refreshableTable));
    }

    private JavaFile buildJavaFile(RefreshableTable refreshableTable) {
        TypeSpec.Builder typeSpecBuilder = createBuilder(refreshableTable);
        addFields(typeSpecBuilder, refreshableTable);
        addAnnotations(typeSpecBuilder, refreshableTable);
        TypeSpec refreshableTableSpec = typeSpecBuilder.build();
        return createJavaFile(refreshableTable, refreshableTableSpec);
    }

    private void addFields(TypeSpec.Builder typeSpecBuilder, RefreshableTable refreshableTable) {
        refreshableTable.getFields().stream().forEach(entityField ->
            typeSpecBuilder.addField(FieldSpec.builder(entityField.getType(), entityField.getName())
                    .addModifiers(Modifier.PRIVATE)
                    .addAnnotation(Column.class).build())
        );
    }

    private void addAnnotations(TypeSpec.Builder typeSpecBuilder, RefreshableTable refreshableTable) {
        typeSpecBuilder.addAnnotation(Data.class);
        typeSpecBuilder.addAnnotation(Generated.class);
        typeSpecBuilder.addAnnotation(AnnotationSpec.builder(Table.class)
                .addMember("value", "\"" + CaseFormat.LOWER_CAMEL.to(
                        CaseFormat.LOWER_UNDERSCORE,
                        refreshableTable.getBaseClass().getSimpleName()) + "\"")
                .build()
        );
    }

    private TypeSpec.Builder createBuilder(RefreshableTable refreshableTable) {
        return TypeSpec.classBuilder(refreshableTable.getBaseClass().getSimpleName())
                .addModifiers(Modifier.PUBLIC)
                .superclass(CarmenNoSQLEntity.class);
    }

    private JavaFile createJavaFile(RefreshableTable refreshableTable, TypeSpec typeSpec) {
        return JavaFile.builder(refreshableTable.getBaseClass().getPackage().getName(), typeSpec)
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

}

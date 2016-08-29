package com.cezarykluczynski.carmen.cron.languages.builder;

import com.cezarykluczynski.carmen.cron.languages.annotations.Annotations;
import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace;
import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;
import com.cezarykluczynski.carmen.cron.languages.api.CassandraEntityBuilder;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.model.CassandraJavaPoetBuiltEntityFile;
import com.cezarykluczynski.carmen.cron.languages.model.EntityField;
import com.cezarykluczynski.carmen.model.CarmenNoSQLEntity;
import com.cezarykluczynski.carmen.model.cassandra.GitDescription;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.squareup.javapoet.*;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.UUID;

@Component
public class CassandraJavaPoetEntityBuilder extends AbstractCassandraMigrationBuilder
        implements CassandraEntityBuilder {

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
        refreshableTable.getFields().stream().forEach(entityField -> addField(typeSpecBuilder, entityField));

        typeSpecBuilder.addMethod(MethodSpec.methodBuilder("getId")
                .addModifiers(Modifier.PUBLIC)
                .returns(UUID.class)
                .addCode("return id;\n")
                .build());
    }

    private static void addField(TypeSpec.Builder typeSpecBuilder, EntityField entityField) {
        FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(entityField.getType(), entityField.getName())
                .addModifiers(Modifier.PUBLIC);

        if (entityField.getType() == UUID.class) {
            fieldSpecBuilder.addAnnotation(PartitionKey.class);
        } else {
            fieldSpecBuilder.addAnnotation(Column.class);
        }

        typeSpecBuilder.addField(fieldSpecBuilder.build());
    }

    private void addAnnotations(TypeSpec.Builder typeSpecBuilder, RefreshableTable refreshableTable) {
        typeSpecBuilder.addAnnotation(AnnotationSpec.builder(Generated.class)
                .addMember("value", "\"" + CassandraJavaPoetEntityBuilder.class.getCanonicalName() + "\"").build());

        Arrays.asList(refreshableTable.getBaseClass().getDeclaredAnnotations()).stream()
                .map(Annotation::annotationType).filter(this::isOwnAnnotation).forEach(typeSpecBuilder::addAnnotation);

        Keyspace keyspace = (Keyspace) refreshableTable.getBaseClass().getAnnotation(Keyspace.class);
        if (keyspace != null) {
            typeSpecBuilder.addAnnotation(AnnotationSpec.builder(Keyspace.class)
                    .addMember("value", "\"" + keyspace.value() + "\"").build());
        }

        typeSpecBuilder.addAnnotation(AnnotationSpec.builder(Table.class)
                .addMember("keyspace", "\"" +  keyspace.value() + "\"")
                .addMember("name", "\"" + getNormalizedTableName(refreshableTable) + "\"")
                .build());
    }

    private TypeSpec.Builder createBuilder(RefreshableTable refreshableTable) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(refreshableTable.getBaseClass().getSimpleName())
                .addModifiers(Modifier.PUBLIC)
                .superclass(CarmenNoSQLEntity.class);

        if (GitDescription.class.isAssignableFrom(refreshableTable.getBaseClass())) {
            builder.addSuperinterface(GitDescription.class);
        }

        return builder;
    }

    private JavaFile createJavaFile(RefreshableTable refreshableTable, TypeSpec typeSpec) {
        return JavaFile.builder(refreshableTable.getBaseClass().getPackage().getName(), typeSpec)
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private boolean isOwnAnnotation(Class clazz) {
        return Annotations.OWN_ANNOTATIONS.contains(clazz);
    }

}

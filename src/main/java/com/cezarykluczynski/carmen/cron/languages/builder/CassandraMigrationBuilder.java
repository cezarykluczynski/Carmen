package com.cezarykluczynski.carmen.cron.languages.builder;

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace;
import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuilder;
import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.model.CassandraBuiltFileNullObject;
import com.cezarykluczynski.carmen.cron.languages.model.CassandraCQLMigrationBuiltFile;
import com.cezarykluczynski.carmen.cron.languages.model.EntityField;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import static com.google.common.base.CaseFormat.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CassandraMigrationBuilder implements CassandraBuilder {

    private static final String EOL = "\n";

    private static final Map<Class, String> CLASS_TO_CASSANDRA_DATA_TYPE_MAPPINGS = Maps.newHashMap();
    static {
        CLASS_TO_CASSANDRA_DATA_TYPE_MAPPINGS.put(String.class, "varchar");
        CLASS_TO_CASSANDRA_DATA_TYPE_MAPPINGS.put(UUID.class, "uuid");
        CLASS_TO_CASSANDRA_DATA_TYPE_MAPPINGS.put(Integer.class, "int");
    }

    private String migrationPath;

    @Autowired
    CassandraMigrationBuilder(@Value("${cassandra.migrationDirectory}") String migrationPath) {
        this.migrationPath = migrationPath;
    }

    @Override
    public CassandraBuiltFile build(RefreshableTable refreshableTable) {
        if (!refreshableTable.hasChanged() && !isNewEntity(refreshableTable)) {
            return new CassandraBuiltFileNullObject();
        }

        String path = buildFileMigrationPath(refreshableTable);
        String contents = buildFileMigrationContents(refreshableTable);
        return new CassandraCQLMigrationBuiltFile(path, contents);
    }

    private String buildFileMigrationPath(RefreshableTable refreshableTable) {
        return new StringBuilder()
                .append(getMigrationPath(refreshableTable))
                .append(getNextVersion(refreshableTable))
                .append("__")
                .append(createActionName(refreshableTable))
                .append("_")
                .append(normalizeTableName(refreshableTable))
                .append("_table.cql")
                .toString();
    }

    /**
     * TODO Change syntax to multiple columns inserted in one statement, when this is resolved:
     * https://issues.apache.org/jira/browse/CASSANDRA-10411
     */
    private String buildFileMigrationContents(RefreshableTable refreshableTable) {
        StringBuilder stringBuilder = new StringBuilder();
        final boolean isNewEntity = isNewEntity(refreshableTable);

        if (isNewEntity) {
            stringBuilder.append(createCreateTableStatement(refreshableTable)).append(EOL);
        }

        if (!refreshableTable.hasChanged()) {
            return stringBuilder.toString();
        }

        String refreshableTableNormalizedName = normalizeTableName(refreshableTable);

        if (isNewEntity) {
            stringBuilder.append(EOL);
        }

        stringBuilder.append(Joiner.on(EOL).join(refreshableTable.getNewFields().stream()
                .map(CassandraMigrationBuilder::createAddColumnStatement)
                .map(newField -> newField = "ALTER TABLE " + refreshableTableNormalizedName + " " + newField)
                .collect(Collectors.toList())));

        stringBuilder.append(EOL);

        return stringBuilder.toString();
    }

    private String getMigrationPath(RefreshableTable refreshableTable) {
        String migrationPathWithKeyspace = migrationPath;
        Keyspace keyspaceAnnotation = (Keyspace) refreshableTable.getBaseClass().getAnnotation(Keyspace.class);

        if (keyspaceAnnotation != null && !keyspaceAnnotation.value().equals("")) {
            migrationPathWithKeyspace += keyspaceAnnotation.value() + "/";
        }

        return migrationPathWithKeyspace;
    }

    private String getNextVersion(RefreshableTable refreshableTable) {
        return "V" + (findLatestMigrationVersion(refreshableTable) + 1) + "_0";
    }

    private int findLatestMigrationVersion(RefreshableTable refreshableTable) {
        List<String> migrationFilePaths = Lists.newArrayList();

        try {
            Files.walk(Paths.get(getMigrationPath(refreshableTable))).filter(Files::isRegularFile)
                    .forEach(filePath -> migrationFilePaths.add(filePath.getFileName().toString()));
        } catch(IOException e) {
        }

        return migrationFilePaths.stream()
                .map(migrationFilePath -> migrationFilePath.split("_"))
                .filter(parts -> parts.length > 1 && String.valueOf(parts[0].charAt(0)).equals("V") &&
                        StringUtils.isNumeric(parts[0].substring(1)))
                .map(parts -> parts[0].substring(1))
                .mapToInt(Integer::valueOf)
                .max().orElse(0);
    }

    private static String createAddColumnStatement(EntityField entityField) {
        return "ADD " + entityField.getName() + " " + getColumnTypeFromClass(entityField.getType()) + ";";
    }

    private static String getColumnTypeFromClass(Class clazz) {
        return CLASS_TO_CASSANDRA_DATA_TYPE_MAPPINGS.get(clazz);
    }

    private  static String createCreateTableStatement(RefreshableTable refreshableTable) {
        return "CREATE TABLE " + normalizeTableName(refreshableTable) + ";";
    }

    private static String createActionName(RefreshableTable refreshableTable) {
        return isNewEntity(refreshableTable) ? "Create" : "Update";
    }

    private static boolean isNewEntity(RefreshableTable refreshableTable) {
        return refreshableTable.getInitialFields().size() == 0;
    }

    private static String normalizeTableName(RefreshableTable refreshableTable) {
        return UPPER_CAMEL.to(LOWER_UNDERSCORE, refreshableTable.getBaseClass().getSimpleName());
    }

}

package com.cezarykluczynski.carmen.cron.languages.builder;

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuilder;
import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import static com.google.common.base.CaseFormat.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class CassandraMigrationBuilder implements CassandraBuilder {

    private String migrationPath;

    CassandraMigrationBuilder(String migrationPath) {
        this.migrationPath = migrationPath;
    }

    @Override
    public CassandraBuiltFile build(RefreshableTable refreshableTable) {
        String path = buildFileMigrationPath(refreshableTable);
        System.out.println("path");
        System.out.println(path);
        String contents = "";
        return new CassandraCQLMigrationBuiltFile(path, contents);
    }

    private String buildFileMigrationPath(RefreshableTable refreshableTable) {
        return new StringBuilder()
                .append(migrationPath)
                .append(getNextVersion())
                .append("__")
                .append(getActionName(refreshableTable))
                .append("_")
                .append(getNormalizedTableName(refreshableTable))
                .append("_table.cql")
                .toString();
    }

    private String getActionName(RefreshableTable refreshableTable) {
        return refreshableTable.getFields().size() == 0 ? "Create" : "Update";
    }

    private String getNormalizedTableName(RefreshableTable refreshableTable) {
        return UPPER_CAMEL.to(LOWER_UNDERSCORE, refreshableTable.getBaseClass().getSimpleName());
    }

    private String getNextVersion() {
        return "V" + (findLatestMigrationVersion() + 1) + "_0";
    }

    private int findLatestMigrationVersion() {
        List<String> migrationFilePaths = Lists.newArrayList();

        try {
            Files.walk(Paths.get(migrationPath)).filter(Files::isRegularFile).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    migrationFilePaths.add(filePath.getFileName().toString());
                }
            });
        } catch(IOException e) {
        }

        return migrationFilePaths.stream()
                .map(migrationFilePath -> migrationFilePath.split("_"))
                .filter(parts -> parts.length > 1 && parts[0].equals("V") && StringUtils.isNumeric(parts[1]))
                .map(parts -> parts[1])
                .mapToInt(Integer::valueOf)
                .max().orElse(0);
    }

}

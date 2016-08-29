package com.cezarykluczynski.carmen.cron.languages.builder;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.datastax.driver.mapping.annotations.Table;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

class AbstractCassandraMigrationBuilder {

    static String getNormalizedTableName(RefreshableTable refreshableTable) {
        Table tableAnnotation = (Table) refreshableTable.getBaseClass().getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.name();
        }

        return UPPER_CAMEL.to(LOWER_UNDERSCORE, refreshableTable.getBaseClass().getSimpleName());
    }

}

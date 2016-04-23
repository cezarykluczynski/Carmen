package com.cezarykluczynski.carmen.cron.languages.visitor;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor;
import com.cezarykluczynski.carmen.cron.languages.builder.CassandraMigrationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchemaUpdaterVisitor implements RefreshableTableVisitor {

    private CassandraMigrationBuilder cassandraMigrationBuilder;

    @Autowired
    public SchemaUpdaterVisitor(CassandraMigrationBuilder cassandraMigrationBuilder) {
        this.cassandraMigrationBuilder = cassandraMigrationBuilder;
    }

    @Override
    public void visit(RefreshableTable refreshableTable) {
        if (refreshableTable.hasChanged()) {
            cassandraMigrationBuilder.build(refreshableTable).save();
        }
    }

}

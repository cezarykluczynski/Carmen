package com.cezarykluczynski.carmen.cron.languages.visitor;

import com.cezarykluczynski.carmen.cron.languages.api.CassandraEntityBuilder;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityUpdaterVisitor implements RefreshableTableVisitor {

    private CassandraEntityBuilder cassandraEntityBuilder;

    @Autowired
    public EntityUpdaterVisitor(CassandraEntityBuilder cassandraEntityBuilder) {
        this.cassandraEntityBuilder = cassandraEntityBuilder;
    }

    @Override
    public void visit(RefreshableTable refreshableTable) {
        if (refreshableTable.hasChanged()) {
            cassandraEntityBuilder.build(refreshableTable).save();
        }
    }

}

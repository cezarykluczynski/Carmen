package com.cezarykluczynski.carmen.cron.languages.visitor;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor;
import com.cezarykluczynski.carmen.cron.languages.model.EntityField;
import org.springframework.stereotype.Component;

import java.util.SortedSet;

@Component
public class SchemaUpdaterVisitor implements RefreshableTableVisitor {

    @Override
    public void visit(RefreshableTable refreshableTable) {
        if (!refreshableTable.hasChanged()) {
            return;
        }

        SortedSet<EntityField> fields = refreshableTable.getFields();
    }

}
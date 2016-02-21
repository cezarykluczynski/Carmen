package com.cezarykluczynski.carmen.cron.languages.filter;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory;
import com.cezarykluczynski.carmen.cron.languages.model.EntityField;

import java.util.SortedSet;
import java.util.TreeSet;

public class AllFieldsFilter implements FieldsFilter {

    @Override
    public SortedSet<EntityField> filterFields(SortedSet<EntityField> fields) {
        TreeSet<EntityField> entityFieldTreeSet = TreeSetEntityFieldFactory.create();
        entityFieldTreeSet.addAll(fields);
        return entityFieldTreeSet;
    }

}

package com.cezarykluczynski.carmen.cron.languages.model;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory;
import com.cezarykluczynski.carmen.cron.languages.iterator.EntityFieldsIterator;

import java.util.SortedSet;

public class RefreshableTableImpl implements RefreshableTable {

    private Class refreshableTableClass;

    private EntityFieldsIterator entityFieldsIterator;

    private SortedSet<EntityField> initialFields;

    private SortedSet<EntityField> fields;

    public RefreshableTableImpl(Class refreshableTableClass) {
        this.refreshableTableClass = refreshableTableClass;
        entityFieldsIterator = new EntityFieldsIterator(refreshableTableClass, FieldsFilter.ALL);
        setFields(entityFieldsIterator.getFields());
        initialFields = entityFieldsIterator.getFields();
    }

    @Override
    public boolean hasChanged() {
        return !initialFields.containsAll(fields);
    }

    @Override
    public void setFields(SortedSet<EntityField> fields) {
        this.fields = TreeSetEntityFieldFactory.copy(fields);
    }

    @Override
    public SortedSet<EntityField> getFields() {
        return TreeSetEntityFieldFactory.copy(fields);
    }

    public SortedSet<EntityField> getNewFields() {
        SortedSet<EntityField> newFields = TreeSetEntityFieldFactory.copy(fields);
        newFields.removeAll(initialFields);
        return newFields;
    }

    @Override
    public Class getBaseClass() {
        return refreshableTableClass;
    }

}

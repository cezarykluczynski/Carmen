package com.cezarykluczynski.carmen.cron.languages.model;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.iterator.EntityFieldsIterator;

import java.util.SortedSet;
import java.util.TreeSet;

public class RefreshableTableImpl implements RefreshableTable {

    private Class refreshableTableClass;

    private SortedSet<String> fields;

    private EntityFieldsIterator entityFieldsIterator;

    private Integer existingFieldsCount;

    private Integer totalFieldsCount;

    public RefreshableTableImpl(Class refreshableTableClass) {
        this.refreshableTableClass = refreshableTableClass;
        entityFieldsIterator = new EntityFieldsIterator(refreshableTableClass, FieldsFilter.ALL);
        fields = entityFieldsIterator.getFields();
        existingFieldsCount = fields.size();
    }

    @Override
    public boolean hasChanged() {
        return totalFieldsCount != null && existingFieldsCount != totalFieldsCount;
    }

    @Override
    public void finalizeFieldsCount() {
        totalFieldsCount = fields.size();
    }

    @Override
    public void setFields(SortedSet<String> fields) {
        this.fields = new TreeSet<>(fields);
    }

    @Override
    public SortedSet<String> getFields() {
        return new TreeSet<>(fields);
    }

    @Override
    public Class getBaseClass() {
        return refreshableTableClass;
    }

}

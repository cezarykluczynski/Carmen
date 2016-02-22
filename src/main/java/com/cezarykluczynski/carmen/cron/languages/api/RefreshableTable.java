package com.cezarykluczynski.carmen.cron.languages.api;

import com.cezarykluczynski.carmen.cron.languages.model.EntityField;

import java.util.SortedSet;

public interface RefreshableTable {

    boolean hasChanged();

    SortedSet<EntityField> getFields();

    SortedSet<EntityField> getNewFields();

    void setFields(SortedSet<EntityField> fields);

    Class getBaseClass();

    default void accept(RefreshableTableVisitor refreshableTableVisitor) {
        refreshableTableVisitor.visit(this);
    }

}

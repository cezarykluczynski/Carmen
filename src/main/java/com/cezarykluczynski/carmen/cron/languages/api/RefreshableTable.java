package com.cezarykluczynski.carmen.cron.languages.api;

import java.util.SortedSet;

public interface RefreshableTable {

    boolean hasChanged();

    void finalizeFieldsCount();

    SortedSet<String> getFields();

    void setFields(SortedSet<String> fields);

    Class getBaseClass();

    default void accept(RefreshableTableVisitor refreshableTableVisitor) {
        refreshableTableVisitor.visit(this);
    }

}

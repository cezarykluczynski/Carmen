package com.cezarykluczynski.carmen.cron.languages.filter;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;

import java.util.SortedSet;
import java.util.TreeSet;

public class AllFieldsFilter implements FieldsFilter {

    @Override
    public SortedSet<String> filterFields(SortedSet<String> fields) {
        return new TreeSet<>(fields);
    }

}

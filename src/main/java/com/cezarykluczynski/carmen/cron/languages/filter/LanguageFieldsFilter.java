package com.cezarykluczynski.carmen.cron.languages.filter;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class LanguageFieldsFilter implements FieldsFilter {

    @Override
    public SortedSet<String> filterFields(SortedSet<String> fields) {
        return new TreeSet<>(fields.stream()
                .filter(field -> field.startsWith("language_") && !field.endsWith("_added") &&
                        !field.endsWith("_removed"))
                .collect(Collectors.toSet()));
    }

}

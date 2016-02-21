package com.cezarykluczynski.carmen.cron.languages.filter;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory;
import com.cezarykluczynski.carmen.cron.languages.model.EntityField;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class LanguageFieldsFilter implements FieldsFilter {

    @Override
    public SortedSet<EntityField> filterFields(SortedSet<EntityField> fields) {
        TreeSet<EntityField> entityFieldTreeSet = TreeSetEntityFieldFactory.create();

        entityFieldTreeSet.addAll(fields.stream()
                .filter(field -> field.getName().startsWith("language_") && !field.getName().endsWith("_added") &&
                        !field.getName().endsWith("_removed"))
                .collect(Collectors.toSet()));

        return entityFieldTreeSet;
    }

}

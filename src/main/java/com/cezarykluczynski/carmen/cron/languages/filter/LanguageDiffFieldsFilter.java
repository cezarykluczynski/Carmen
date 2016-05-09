package com.cezarykluczynski.carmen.cron.languages.filter;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory;
import com.cezarykluczynski.carmen.cron.languages.model.EntityField;
import com.cezarykluczynski.carmen.cron.languages.util.FieldNameUtil;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class LanguageDiffFieldsFilter implements FieldsFilter {

    @Override
    public SortedSet<EntityField> filterFields(SortedSet<EntityField> fields) {
        TreeSet<EntityField> entityFieldTreeSet = TreeSetEntityFieldFactory.create();

        entityFieldTreeSet.addAll(fields.stream()
                .filter(field -> FieldNameUtil.isLanguageAddedOrRemoved(field.getName()))
                .collect(Collectors.toSet()));

        return entityFieldTreeSet;
    }

}

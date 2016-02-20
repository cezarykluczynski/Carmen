package com.cezarykluczynski.carmen.cron.languages.iterator;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;

public class EntityFieldsIterator implements Iterator<String> {

    private Class clazz;

    private SortedSet<String> fields;

    private Iterator<String> iterator;

    private FieldsFilter fieldsFilter;

    public EntityFieldsIterator(Class clazz, FieldsFilter fieldsFilter) {
        this.clazz = clazz;
        this.fieldsFilter = fieldsFilter;
        createChildIterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public String next() {
        return iterator.next();
    }

    public SortedSet<String> getFields() {
        return fields;
    }

    private void createChildIterator() {
        List<String> fieldsList = Arrays.asList(clazz.getDeclaredFields()).stream()
                .filter(field -> !field.isSynthetic())
                .map(Field::getName).collect(Collectors.toList());
        fieldsList.sort(naturalOrder());
        fields = fieldsFilter.filterFields(new TreeSet<>(fieldsList));
        iterator = fields.iterator();
    }
}

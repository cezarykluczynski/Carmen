package com.cezarykluczynski.carmen.cron.languages.iterator;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory;
import com.cezarykluczynski.carmen.cron.languages.model.EntityField;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EntityFieldsIterator implements Iterator<EntityField> {

    private Class clazz;

    private SortedSet<EntityField> fields;

    private Iterator<EntityField> iterator;

    private FieldsFilter fieldsFilter;

    EntityFieldsIterator(Class clazz, FieldsFilter fieldsFilter) {
        this.clazz = clazz;
        this.fieldsFilter = fieldsFilter;
        createChildIterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public EntityField next() {
        return iterator.next();
    }

    public SortedSet<EntityField> getFields() {
        return TreeSetEntityFieldFactory.copy(fields);
    }

    private void createChildIterator() {
        List<Field> fieldsList = Arrays.asList(clazz.getDeclaredFields()).stream()
                .filter(field -> !field.isSynthetic())
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .collect(Collectors.toList());

        TreeSet<EntityField> entityFieldTreeSet = TreeSetEntityFieldFactory.create();

        fieldsList.stream().forEach(field ->
            entityFieldTreeSet.add(new EntityField(field.getName(), field.getType()))
        );

        fields = fieldsFilter.filterFields(entityFieldTreeSet);
        iterator = fields.iterator();
    }

}

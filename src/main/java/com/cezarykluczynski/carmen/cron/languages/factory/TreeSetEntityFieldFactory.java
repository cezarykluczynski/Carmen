package com.cezarykluczynski.carmen.cron.languages.factory;

import com.cezarykluczynski.carmen.cron.languages.model.EntityField;

import java.util.Comparator;
import java.util.TreeSet;

public class TreeSetEntityFieldFactory {

    public static TreeSet<EntityField> create() {
        return new TreeSet<>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                EntityField entityField1 = (EntityField) o1;
                EntityField entityField2 = (EntityField) o2;

                return entityField1.getName().compareTo(entityField2.getName());
            }
        });
    }

}

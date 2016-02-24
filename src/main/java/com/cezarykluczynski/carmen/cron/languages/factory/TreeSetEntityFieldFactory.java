package com.cezarykluczynski.carmen.cron.languages.factory;

import com.cezarykluczynski.carmen.cron.languages.model.EntityField;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class TreeSetEntityFieldFactory {

    public static TreeSet<EntityField> create() {
        return new TreeSet<>(new Comparator<EntityField>() {
            @Override
            public int compare(EntityField entityField1, EntityField entityField2) {
                String name1 = entityField1.getName();
                String name2 = entityField2.getName();

                if (name1.equals(name2)) {
                    return 0;
                }

                if (nameAreLanguage(name1, name2)) {
                    Integer languageComparison = compareLanguages(name1, name2);

                    if (languageComparison != null) {
                        return languageComparison;
                    }
                }

                return entityField1.getName().compareTo(entityField2.getName());
            }
        });
    }

    public static SortedSet<EntityField> copy(SortedSet<EntityField> entityFieldSortedSet) {
        SortedSet<EntityField> entityFieldSortedSetCopy = create();
        entityFieldSortedSetCopy.addAll(entityFieldSortedSet);
        return entityFieldSortedSetCopy;
    }

    private static Integer compareLanguages(String name1, String name2) {
        Integer language1Number = Integer.valueOf(name1.substring(9).split("_")[0]);
        Integer language2Number = Integer.valueOf(name2.substring(9).split("_")[0]);

        if (language1Number.compareTo(language2Number) != 0) {
            return language1Number.compareTo(language2Number);
        } else if (nameIsLanguage(name1) && !nameIsLanguage(name2)) {
            return -1;
        } else if (!nameIsLanguage(name1) && nameIsLanguage(name2)) {
            return 1;
        } else if (nameIsAdded(name1) && nameIsRemoved(name2)) {
            return -1;
        } else if (nameIsRemoved(name1) && nameIsAdded(name2)) {
            return 1;
        }

        return null;
    }

    private static boolean nameAreLanguage(String name1, String name2) {
        return nameIsAnyLanguage(name1) && nameIsAnyLanguage(name2);
    }

    private static boolean nameIsLanguage(String name) {
        return nameIsAnyLanguage(name) && !nameIsAdded(name) && !nameIsRemoved(name);
    }

    private static boolean nameIsAnyLanguage(String name) {
        return name.startsWith("language_");
    }

    private static boolean nameIsAdded(String name) {
        return name.endsWith("_added");
    }

    private static boolean nameIsRemoved(String name) {
        return name.endsWith("_removed");
    }

}

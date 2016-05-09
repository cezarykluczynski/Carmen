package com.cezarykluczynski.carmen.cron.languages.factory;

import com.cezarykluczynski.carmen.cron.languages.model.EntityField;
import com.cezarykluczynski.carmen.cron.languages.util.FieldNameUtil;

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

                if (namesAreAnyLanguage(name1, name2)) {
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
        } else if (nameIsAnyLanguage(name1) && !nameIsAnyLanguage(name2)) {
            return -1;
        } else if (!nameIsAnyLanguage(name1) && nameIsAnyLanguage(name2)) {
            return 1;
        } else if (nameIsLanguageAdded(name1) && nameIsLanguageRemoved(name2)) {
            return -1;
        } else if (nameIsLanguageRemoved(name1) && nameIsLanguageAdded(name2)) {
            return 1;
        }

        return null;
    }

    private static boolean namesAreAnyLanguage(String name1, String name2) {
        return FieldNameUtil.isAnyLanguage(name1) && FieldNameUtil.isAnyLanguage(name2);
    }

    private static boolean nameIsAnyLanguage(String name) {
        return FieldNameUtil.isAnyLanguage(name) && !FieldNameUtil.isLanguageAddedOrRemoved(name);
    }

    private static boolean nameIsLanguageAdded(String name) {
        return FieldNameUtil.isLanguageAdded(name);
    }

    private static boolean nameIsLanguageRemoved(String name) {
        return FieldNameUtil.isLanguageRemoved(name);
    }

}

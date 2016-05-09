package com.cezarykluczynski.carmen.cron.languages.util;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class FieldNameUtil {

    private static final Pattern LANGUAGE = Pattern.compile("^language_\\d+$");
    private static final Pattern LANGUAGE_ADDED = Pattern.compile("^language_\\d+_added$");
    private static final Pattern LANGUAGE_REMOVED = Pattern.compile("^language_\\d+_removed$");

    public static boolean isLanguage(Field field) {
        return field != null && isLanguage(field.getName());
    }

    public static boolean isLanguage(String fieldName) {
        return fieldName != null && LANGUAGE.matcher(fieldName).matches();
    }

    public static boolean isLanguageAdded(Field field) {
        return field != null && isLanguageAdded(field.getName());
    }

    public static boolean isLanguageAdded(String fieldName) {
        return fieldName != null && LANGUAGE_ADDED.matcher(fieldName).matches();
    }

    public static boolean isLanguageRemoved(Field field) {
        return field != null && isLanguageRemoved(field.getName());
    }

    public static boolean isLanguageRemoved(String fieldName) {
        return fieldName != null && LANGUAGE_REMOVED.matcher(fieldName).matches();
    }

    public static boolean isLanguageAddedOrRemoved(Field field) {
        return field != null && isLanguageAddedOrRemoved(field.getName());
    }

    public static boolean isLanguageAddedOrRemoved(String fieldName) {
        return fieldName != null && (isLanguageAdded(fieldName) || isLanguageRemoved(fieldName));
    }

    public static boolean isAnyLanguage(Field field) {
        return field != null && isAnyLanguage(field.getName());
    }

    public static boolean isAnyLanguage(String fieldName) {
        return fieldName != null && (isLanguageAddedOrRemoved(fieldName) || isLanguage(fieldName));
    }

}

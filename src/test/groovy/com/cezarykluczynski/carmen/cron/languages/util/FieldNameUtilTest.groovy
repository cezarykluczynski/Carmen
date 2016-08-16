package com.cezarykluczynski.carmen.cron.languages.util

import spock.lang.Specification

import java.lang.reflect.Field

class FieldNameUtilTest extends Specification {

    def "isLanguage correctly detects language field names"() {
        expect:
        FieldNameUtil.isLanguage(createFiledWithName("language_5"))
        FieldNameUtil.isLanguage(createFiledWithName("language_500"))
        !FieldNameUtil.isLanguage(createFiledWithName("language_5_added"))
        !FieldNameUtil.isLanguage(createFiledWithName("language_500_added"))
        !FieldNameUtil.isLanguage(createFiledWithName("language_5_removed"))
        !FieldNameUtil.isLanguage(createFiledWithName("language_500_removed"))
        !FieldNameUtil.isLanguage(createFiledWithName("language_500_gibberish"))
        !FieldNameUtil.isLanguage(createFiledWithName("gibberish"))
        !FieldNameUtil.isLanguage((Field) null)
        !FieldNameUtil.isLanguage((String) null)
    }

    def "isLanguageAdded correctly detects language field names"() {
        expect:
        FieldNameUtil.isLanguageAdded(createFiledWithName("language_1_added"))
        FieldNameUtil.isLanguageAdded(createFiledWithName("language_100_added"))
        !FieldNameUtil.isLanguageAdded(createFiledWithName("language_1"))
        !FieldNameUtil.isLanguageAdded(createFiledWithName("language_100_removed"))
        !FieldNameUtil.isLanguageAdded(createFiledWithName("language_1"))
        !FieldNameUtil.isLanguageAdded(createFiledWithName("language_100_removed"))
        !FieldNameUtil.isLanguageAdded(createFiledWithName("gibberish"))
        !FieldNameUtil.isLanguageAdded((Field) null)
        !FieldNameUtil.isLanguageAdded((String) null)

    }

    def "isLanguageRemoved correctly detects language field names"() {
        expect:
        FieldNameUtil.isLanguageRemoved(createFiledWithName("language_2_removed"))
        FieldNameUtil.isLanguageRemoved(createFiledWithName("language_200_removed"))
        !FieldNameUtil.isLanguageRemoved(createFiledWithName("language_2"))
        !FieldNameUtil.isLanguageRemoved(createFiledWithName("language_200"))
        !FieldNameUtil.isLanguageRemoved(createFiledWithName("language_2_added"))
        !FieldNameUtil.isLanguageRemoved(createFiledWithName("language_200_added"))
        !FieldNameUtil.isLanguageRemoved(createFiledWithName("gibberish"))
        !FieldNameUtil.isLanguageRemoved((Field) null)
        !FieldNameUtil.isLanguageRemoved((String) null)
    }

    def "isAnyLanguage correctly detects language field names"() {
        expect:
        FieldNameUtil.isAnyLanguage(createFiledWithName("language_3_removed"))
        FieldNameUtil.isAnyLanguage(createFiledWithName("language_3_added"))
        FieldNameUtil.isAnyLanguage(createFiledWithName("language_300_removed"))
        FieldNameUtil.isAnyLanguage(createFiledWithName("language_300_added"))
        FieldNameUtil.isAnyLanguage(createFiledWithName("language_3"))
        FieldNameUtil.isAnyLanguage(createFiledWithName("language_300"))
        !FieldNameUtil.isAnyLanguage(createFiledWithName("language_300_gibberish"))
        !FieldNameUtil.isAnyLanguage(createFiledWithName("gibberish"))
        !FieldNameUtil.isAnyLanguage((Field) null)
        !FieldNameUtil.isAnyLanguage((String) null)
    }

    def "isLanguageAddedOrRemoved correctly detects language field names"() {
        expect:
        FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_4_removed"))
        FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_4_added"))
        FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_400_removed"))
        FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_400_added"))
        !FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_4"))
        !FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_400"))
        !FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_300_gibberish"))
        !FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("gibberish"))
        !FieldNameUtil.isLanguageAddedOrRemoved((Field) null)
        !FieldNameUtil.isLanguageAddedOrRemoved((String) null)
    }

    private static Field createFiledWithName(String name) {
        return new Field(void.class, name, void.class, 0, 0, "")
    }

}

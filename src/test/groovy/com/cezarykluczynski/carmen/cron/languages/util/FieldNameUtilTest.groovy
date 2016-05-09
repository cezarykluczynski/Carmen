package com.cezarykluczynski.carmen.cron.languages.util

import org.testng.Assert
import org.testng.annotations.Test

import java.lang.reflect.Field

class FieldNameUtilTest {

    @Test
    void isLanguage() {
        Assert.assertTrue FieldNameUtil.isLanguage(createFiledWithName("language_5"))
        Assert.assertTrue FieldNameUtil.isLanguage(createFiledWithName("language_500"))

        Assert.assertFalse FieldNameUtil.isLanguage(createFiledWithName("language_5_added"))
        Assert.assertFalse FieldNameUtil.isLanguage(createFiledWithName("language_500_added"))
        Assert.assertFalse FieldNameUtil.isLanguage(createFiledWithName("language_5_removed"))
        Assert.assertFalse FieldNameUtil.isLanguage(createFiledWithName("language_500_removed"))
        Assert.assertFalse FieldNameUtil.isLanguage(createFiledWithName("language_500_gibberish"))
        Assert.assertFalse FieldNameUtil.isLanguage(createFiledWithName("gibberish"))
        Assert.assertFalse FieldNameUtil.isLanguage((Field) null)
        Assert.assertFalse FieldNameUtil.isLanguage((String) null)
    }

    @Test
    void isLanguageAdded() {
        Assert.assertTrue FieldNameUtil.isLanguageAdded(createFiledWithName("language_1_added"))
        Assert.assertTrue FieldNameUtil.isLanguageAdded(createFiledWithName("language_100_added"))

        Assert.assertFalse FieldNameUtil.isLanguageAdded(createFiledWithName("language_1"))
        Assert.assertFalse FieldNameUtil.isLanguageAdded(createFiledWithName("language_100_removed"))
        Assert.assertFalse FieldNameUtil.isLanguageAdded(createFiledWithName("language_1"))
        Assert.assertFalse FieldNameUtil.isLanguageAdded(createFiledWithName("language_100_removed"))
        Assert.assertFalse FieldNameUtil.isLanguageAdded(createFiledWithName("gibberish"))
        Assert.assertFalse FieldNameUtil.isLanguageAdded((Field) null)
        Assert.assertFalse FieldNameUtil.isLanguageAdded((String) null)

    }

    @Test
    void isLanguageRemoved() {
        Assert.assertTrue FieldNameUtil.isLanguageRemoved(createFiledWithName("language_2_removed"))
        Assert.assertTrue FieldNameUtil.isLanguageRemoved(createFiledWithName("language_200_removed"))

        Assert.assertFalse FieldNameUtil.isLanguageRemoved(createFiledWithName("language_2"))
        Assert.assertFalse FieldNameUtil.isLanguageRemoved(createFiledWithName("language_200"))
        Assert.assertFalse FieldNameUtil.isLanguageRemoved(createFiledWithName("language_2_added"))
        Assert.assertFalse FieldNameUtil.isLanguageRemoved(createFiledWithName("language_200_added"))
        Assert.assertFalse FieldNameUtil.isLanguageRemoved(createFiledWithName("gibberish"))
        Assert.assertFalse FieldNameUtil.isLanguageRemoved((Field) null)
        Assert.assertFalse FieldNameUtil.isLanguageRemoved((String) null)
    }

    @Test
    void isAnyLanguage() {
        Assert.assertTrue FieldNameUtil.isAnyLanguage(createFiledWithName("language_3_removed"))
        Assert.assertTrue FieldNameUtil.isAnyLanguage(createFiledWithName("language_3_added"))
        Assert.assertTrue FieldNameUtil.isAnyLanguage(createFiledWithName("language_300_removed"))
        Assert.assertTrue FieldNameUtil.isAnyLanguage(createFiledWithName("language_300_added"))
        Assert.assertTrue FieldNameUtil.isAnyLanguage(createFiledWithName("language_3"))
        Assert.assertTrue FieldNameUtil.isAnyLanguage(createFiledWithName("language_300"))

        Assert.assertFalse FieldNameUtil.isAnyLanguage(createFiledWithName("language_300_gibberish"))
        Assert.assertFalse FieldNameUtil.isAnyLanguage(createFiledWithName("gibberish"))
        Assert.assertFalse FieldNameUtil.isAnyLanguage((Field) null)
        Assert.assertFalse FieldNameUtil.isAnyLanguage((String) null)
    }

    @Test
    void isLanguageAddedOrRemoved() {
        Assert.assertTrue FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_4_removed"))
        Assert.assertTrue FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_4_added"))
        Assert.assertTrue FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_400_removed"))
        Assert.assertTrue FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_400_added"))

        Assert.assertFalse FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_4"))
        Assert.assertFalse FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_400"))
        Assert.assertFalse FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("language_300_gibberish"))
        Assert.assertFalse FieldNameUtil.isLanguageAddedOrRemoved(createFiledWithName("gibberish"))
        Assert.assertFalse FieldNameUtil.isLanguageAddedOrRemoved((Field) null)
        Assert.assertFalse FieldNameUtil.isLanguageAddedOrRemoved((String) null)
    }

    private static Field createFiledWithName(String name) {
        return new Field(void.class, name, void.class, 0, 0, "")
    }

}

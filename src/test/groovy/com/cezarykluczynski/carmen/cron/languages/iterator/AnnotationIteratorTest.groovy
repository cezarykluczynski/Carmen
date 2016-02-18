package com.cezarykluczynski.carmen.cron.languages.iterator

import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class AnnotationIteratorTest {

    AnnotationIterator annotationIterator

    @BeforeMethod
    void setUp() {
        annotationIterator = new AnnotationIterator()
    }

    @Test
    void annotationsAreDiscovered() {
        Assert.assertTrue annotationIterator.next() == LanguagesDiffStatistics.class
        Assert.assertTrue annotationIterator.next() == LanguagesStatistics.class

        Assert.assertFalse annotationIterator.hasNext()
    }

}

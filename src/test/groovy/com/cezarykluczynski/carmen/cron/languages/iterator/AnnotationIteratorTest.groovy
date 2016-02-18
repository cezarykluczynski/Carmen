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
        Class firstAnnotation = annotationIterator.next()
        Class secondAnnotation = annotationIterator.next()
        Assert.assertTrue firstAnnotation == LanguagesDiffStatistics.class || firstAnnotation == LanguagesStatistics.class
        Assert.assertTrue secondAnnotation == LanguagesDiffStatistics.class || secondAnnotation == LanguagesStatistics.class

        Assert.assertFalse annotationIterator.hasNext()
    }

}

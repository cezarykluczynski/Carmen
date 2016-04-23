package com.cezarykluczynski.carmen.cron.languages.iterator

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class AnnotationIteratorTest {

    AnnotationIterator annotationIterator

    @BeforeMethod
    void setUp() {
        annotationIterator = new LanguagesIteratorsFactory().createAnnotationIterator()
    }

    @Test
    void annotationsAreDiscovered() {
        boolean languageDiffStatisticsDiscovered = false
        boolean languageStatisticsDiscovered = false
        boolean keyspaceDiscovered = false

        while(annotationIterator.hasNext()) {
            Class annotation = annotationIterator.next()

            if (annotation == LanguagesDiffStatistics) {
                languageDiffStatisticsDiscovered = true
            }

            if (annotation == LanguagesStatistics) {
                languageStatisticsDiscovered = true
            }

            if (annotation == Keyspace) {
                keyspaceDiscovered = true
            }

        }

        Assert.assertTrue languageDiffStatisticsDiscovered
        Assert.assertTrue languageStatisticsDiscovered
        Assert.assertTrue keyspaceDiscovered

        Assert.assertFalse annotationIterator.hasNext()
    }

}

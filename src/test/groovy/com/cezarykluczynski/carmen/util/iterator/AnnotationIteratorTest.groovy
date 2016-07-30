package com.cezarykluczynski.carmen.util.iterator

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import spock.lang.Specification

class AnnotationIteratorTest extends Specification {

    private AnnotationIterator annotationIterator

    void setup() {
        annotationIterator = new LanguagesIteratorsFactory().createLanguagesAnnotationIterator()
    }

    def "annotations are discovered"() {
        given:
        boolean languageDiffStatisticsDiscovered = false
        boolean languageStatisticsDiscovered = false
        boolean keyspaceDiscovered = false

        when:
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

        then:
        languageDiffStatisticsDiscovered
        languageStatisticsDiscovered
        keyspaceDiscovered
        !annotationIterator.hasNext()
    }

}

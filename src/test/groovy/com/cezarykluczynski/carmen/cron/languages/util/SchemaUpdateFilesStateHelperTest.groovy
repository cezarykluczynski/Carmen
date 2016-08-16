package com.cezarykluczynski.carmen.cron.languages.util

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.fixture.annotation.FixtureAnnotation
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityWithFixtureAnnotation
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesAnnotationIterator
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import com.cezarykluczynski.carmen.cron.languages.iterator.RefreshableTableIterator
import com.cezarykluczynski.carmen.util.exec.executor.Executor
import com.cezarykluczynski.carmen.vcs.git.util.GitCommand
import spock.lang.Specification

class SchemaUpdateFilesStateHelperTest extends Specification {

    private static final String ENTITY_PATH = "src/test/groovy/com/cezarykluczynski/carmen/cron/languages/fixture/entity/EntityWithFixtureAnnotation.groovy"

    private static final String CHANGE_TEXT = "change"

    SchemaUpdateFilesStateHelper schemaUpdateFilesStateHelper

    def setup() {
        Executor.execute(new GitCommand("checkout HEAD " + ENTITY_PATH))

        LanguagesAnnotationIterator annotationIterator = Mock LanguagesAnnotationIterator
        annotationIterator.hasNext() >>> [true, false]
        annotationIterator.next() >> FixtureAnnotation.class

        RefreshableTable refreshableTable = Mock RefreshableTable
        refreshableTable.getBaseClass() >> EntityWithFixtureAnnotation.class

        RefreshableTableIterator refreshableTableIterator = Mock RefreshableTableIterator
        refreshableTableIterator.hasNext() >>> [true, false]
        refreshableTableIterator.next() >> refreshableTable

        LanguagesIteratorsFactory annotationIteratorFactory = Mock LanguagesIteratorsFactory
        annotationIteratorFactory.createLanguagesAnnotationIterator() >> annotationIterator
        annotationIteratorFactory.createRefreshableTableIterator(FixtureAnnotation.class) >> refreshableTableIterator

        schemaUpdateFilesStateHelper = new SchemaUpdateFilesStateHelper(
                annotationIteratorFactory, "src/test/groovy/", "groovy")
    }

    def cleanup() {
        Executor.execute(new GitCommand("checkout HEAD " + ENTITY_PATH))
    }

    def "should detect unchanged files"() {
        expect:
        !schemaUpdateFilesStateHelper.hasFilesChanged()
    }

    def "should detect changed files"() {
        given:
        File file = new File(ENTITY_PATH)
        file.append CHANGE_TEXT

        expect:
        schemaUpdateFilesStateHelper.hasFilesChanged()
    }


}

package com.cezarykluczynski.carmen.cron.languages.util

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.fixture.annotation.FixtureAnnotation
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityWithFixtureAnnotation
import com.cezarykluczynski.carmen.cron.languages.iterator.AnnotationIterator
import com.cezarykluczynski.carmen.cron.languages.iterator.RefreshableTableIterator
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import com.cezarykluczynski.carmen.util.exec.Executor
import com.cezarykluczynski.carmen.vcs.git.util.GitCommand
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class SchemaUpdateFilesStateHelperTest {

    private static final String ENTITY_PATH = "src/test/groovy/com/cezarykluczynski/carmen/cron/languages/fixture/entity/EntityWithFixtureAnnotation.groovy"

    private static final String CHANGE_TEXT = "change"

    SchemaUpdateFilesStateHelper schemaUpdateFilesStateHelper

    @AfterMethod
    void tearDown() {
        Executor.execute(new GitCommand("checkout HEAD " + ENTITY_PATH))
    }

    @BeforeMethod
    void setUp() {
        Executor.execute(new GitCommand("checkout HEAD " + ENTITY_PATH))

        AnnotationIterator annotationIterator = mock AnnotationIterator.class
        when annotationIterator.hasNext() thenReturn true, false
        when annotationIterator.next() thenReturn FixtureAnnotation.class

        RefreshableTable refreshableTable = mock RefreshableTable.class
        when refreshableTable.getBaseClass() thenReturn EntityWithFixtureAnnotation.class

        RefreshableTableIterator refreshableTableIterator = mock RefreshableTableIterator.class
        when refreshableTableIterator.hasNext() thenReturn true, false
        when refreshableTableIterator.next() thenReturn refreshableTable

        LanguagesIteratorsFactory annotationIteratorFactory = mock LanguagesIteratorsFactory.class
        when annotationIteratorFactory.createAnnotationIterator() thenReturn annotationIterator
        when annotationIteratorFactory.createRefreshableTableIterator(FixtureAnnotation.class) thenReturn refreshableTableIterator

        schemaUpdateFilesStateHelper = new SchemaUpdateFilesStateHelper(
                annotationIteratorFactory, "src/test/groovy/", "groovy")
    }

    @Test
    void "should detect unchanged files"() {
        Assert.assertFalse schemaUpdateFilesStateHelper.hasFilesChanged()
    }

    @Test
    void "should detect changed files"() {
        File file = new File(ENTITY_PATH)
        file.append CHANGE_TEXT
        Assert.assertTrue schemaUpdateFilesStateHelper.hasFilesChanged()
    }


}

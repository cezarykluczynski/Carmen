package com.cezarykluczynski.carmen.cron.languages.builder

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityTwo
import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import com.cezarykluczynski.carmen.cron.languages.model.RefreshableTableImpl
import org.apache.commons.io.FileUtils
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class CassandraJavaPoetEntityBuilderTest {

    private CassandraJavaPoetEntityBuilder cassandraJavaPoetEntityBuilder

    private RefreshableTable refreshableTable

    private CassandraBuiltFile cassandraBuiltFile

    private String expectedContent = '''\
package com.cezarykluczynski.carmen.cron.languages.fixture.entity;

import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics;
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics;
import com.cezarykluczynski.carmen.model.CarmenNoSQLEntity;
import java.util.UUID;
import javax.annotation.Generated;
import lombok.Data;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Table;

@Data
@Generated("com.cezarykluczynski.carmen.cron.languages.builder.CassandraJavaPoetEntityBuilder")
@LanguagesStatistics
@LanguagesDiffStatistics
@Table("entity_two")
public class EntityTwo extends CarmenNoSQLEntity {
    @Column
    private UUID id;

    @Column
    private Integer language_1;

    @Column
    private Integer language_1_added;

    @Column
    private Integer language_1_removed;

    @Column
    private Integer language_2;

    @Column
    private Integer language_2_added;

    @Column
    private Integer language_2_removed;
}
'''

    private String expectedPath = "src/main/java/com/cezarykluczynski/carmen/cron/languages/fixture/entity/EntityTwo.java"

    @BeforeMethod
    void setUp() {
        cassandraJavaPoetEntityBuilder = new CassandraJavaPoetEntityBuilder()
        refreshableTable = new RefreshableTableImpl(EntityTwo.class)
        TreeSet<EntityField> entityFieldTreeSet = TreeSetEntityFieldFactory.create()
        entityFieldTreeSet.add new EntityField("id", UUID.class)
        entityFieldTreeSet.add new EntityField("language_1")
        entityFieldTreeSet.add new EntityField("language_1_added")
        entityFieldTreeSet.add new EntityField("language_1_removed")
        entityFieldTreeSet.add new EntityField("language_2")
        entityFieldTreeSet.add new EntityField("language_2_added")
        entityFieldTreeSet.add new EntityField("language_2_removed")
        refreshableTable.setFields entityFieldTreeSet
        cassandraBuiltFile = cassandraJavaPoetEntityBuilder.build refreshableTable
    }

    @Test
    void "entity has the right content and is saved in the right place"() {
        String content = cassandraBuiltFile.getContents()
        String path = cassandraBuiltFile.getPath()

        Assert.assertEquals content, expectedContent
        Assert.assertEquals path, expectedPath

        cassandraBuiltFile.save()

        String fileContent = FileUtils.readFileToString new File("./" + expectedPath)

        Assert.assertEquals fileContent, expectedContent
    }

    @AfterMethod
    void tearDown() {
        FileUtils.deleteQuietly new File("./" + expectedPath)
    }

}

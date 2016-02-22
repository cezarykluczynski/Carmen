package com.cezarykluczynski.carmen.cron.languages.builder

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory
import com.cezarykluczynski.carmen.cron.languages.fixture.EntityTwo
import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import com.cezarykluczynski.carmen.cron.languages.model.RefreshableTableImpl
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class CassandraJavaPoetEntityBuilderTest {

    private CassandraJavaPoetEntityBuilder cassandraJavaPoetEntityBuilder

    private RefreshableTable refreshableTable

    private String expectedContent = '''\
package com.cezarykluczynski.carmen.cron.languages.fixture;

import com.cezarykluczynski.carmen.model.CarmenNoSQLEntity;
import javax.annotation.Generated;
import lombok.Data;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Table;

@Data
@Generated
@Table("entity_two")
public class EntityTwo extends CarmenNoSQLEntity {
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

    @BeforeMethod
    void setUp() {
        cassandraJavaPoetEntityBuilder = new CassandraJavaPoetEntityBuilder()
        refreshableTable = new RefreshableTableImpl(EntityTwo.class)
        TreeSet<EntityField> entityFieldTreeSet = TreeSetEntityFieldFactory.create()
        entityFieldTreeSet.add new EntityField("language_1")
        entityFieldTreeSet.add new EntityField("language_1_added")
        entityFieldTreeSet.add new EntityField("language_1_removed")
        entityFieldTreeSet.add new EntityField("language_2")
        entityFieldTreeSet.add new EntityField("language_2_added")
        entityFieldTreeSet.add new EntityField("language_2_removed")
        refreshableTable.setFields entityFieldTreeSet
    }

    @Test
    void "entity is built"() {
        CassandraBuiltFile cassandraBuiltFile = cassandraJavaPoetEntityBuilder.build refreshableTable

        Assert.assertEquals cassandraBuiltFile.getContents(), expectedContent
    }

}

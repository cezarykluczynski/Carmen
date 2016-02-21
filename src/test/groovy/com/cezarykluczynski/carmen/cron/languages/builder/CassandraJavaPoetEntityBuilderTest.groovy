package com.cezarykluczynski.carmen.cron.languages.builder

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.iterator.fixture.Entity2
import com.cezarykluczynski.carmen.cron.languages.model.RefreshableTableImpl
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class CassandraJavaPoetEntityBuilderTest {

    private CassandraJavaPoetEntityBuilder cassandraJavaPoetEntityBuilder

    private RefreshableTable refreshableTable

    private String expectedContent = '''\
package com.cezarykluczynski.carmen.cron.languages.iterator.fixture;

public class Entity2 {
}
'''

    @BeforeMethod
    void setUp() {
        cassandraJavaPoetEntityBuilder = new CassandraJavaPoetEntityBuilder()
        refreshableTable = new RefreshableTableImpl(Entity2.class)
    }

    @Test
    void "entity is built"() {
        CassandraBuiltFile cassandraBuiltFile = cassandraJavaPoetEntityBuilder.build refreshableTable

        Assert.assertEquals cassandraBuiltFile.getContents(), expectedContent
    }

}

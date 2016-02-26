package com.cezarykluczynski.carmen.cron.languages.builder

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityEmpty
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.model.RefreshableTableImpl
import org.junit.Assert
import org.testng.annotations.Test

class CassandraMigrationBuilderTest {

    private static final String testMigrationDirectory = "src/test/groovy/com/cezarykluczynski/carmen/cron/languages/fixture/migration/"

    private static final String emptyMigrationDirectory = "src/test/groovy/com/cezarykluczynski/carmen/cron/languages/fixture/migration/empty/"

    CassandraMigrationBuilder cassandraMigrationBuilder

    @Test
    void "path is built for existing entity in non-empty folder"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(testMigrationDirectory)
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(new RefreshableTableImpl(EntityOne.class))
        Assert.assertEquals testMigrationDirectory + "V2_0__Update_entity_one_table.cql", cassandraBuiltFile.getPath()
    }

    @Test
    void "path is build for existing entity in empty folder"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(emptyMigrationDirectory)
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(new RefreshableTableImpl(EntityOne.class))
        Assert.assertEquals emptyMigrationDirectory + "V1_0__Update_entity_one_table.cql", cassandraBuiltFile.getPath()
    }

    @Test
    void "path is built for empty entity in non-empty folder"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(testMigrationDirectory)
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(new RefreshableTableImpl(EntityEmpty.class))
        Assert.assertEquals testMigrationDirectory + "V2_0__Create_entity_empty_table.cql", cassandraBuiltFile.getPath()
    }

    @Test
    void "path is built for empty entity in empty folder"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(emptyMigrationDirectory)
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(new RefreshableTableImpl(EntityEmpty.class))
        Assert.assertEquals emptyMigrationDirectory + "V1_0__Create_entity_empty_table.cql", cassandraBuiltFile.getPath()
    }

}

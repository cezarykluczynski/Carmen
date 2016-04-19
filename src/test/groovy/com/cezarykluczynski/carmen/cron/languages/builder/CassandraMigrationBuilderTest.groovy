package com.cezarykluczynski.carmen.cron.languages.builder

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityEmpty
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.model.CassandraBuiltFileNullObject
import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import com.cezarykluczynski.carmen.cron.languages.model.RefreshableTableImpl
import org.junit.Assert
import org.testng.annotations.Test

class CassandraMigrationBuilderTest {

    private static final String TEST_MIGRATION_DIRECTORY =
            "src/test/groovy/com/cezarykluczynski/carmen/cron/languages/fixture/migration/"
    private static final String EMPTY_MIGRATION_DIRECTORY =
            "src/test/groovy/com/cezarykluczynski/carmen/cron/languages/fixture/migration/empty/"
    private static final String NEW_ENTITY_CONTENTS_WITHOUT_NEW_FIELDS = '''\
CREATE TABLE entity_empty;
'''
    private static final String NEW_ENTITY_CONTENTS_WITH_NEW_FIELDS = '''\
CREATE TABLE entity_empty;

ALTER TABLE entity_empty ADD language_100 int;
ALTER TABLE entity_empty ADD some_string varchar;
ALTER TABLE entity_empty ADD some_uuid uuid;
'''
    private static final String EXISTING_ENTITY_CONTENTS = '''\
ALTER TABLE entity_one ADD language_100 int;
ALTER TABLE entity_one ADD some_string varchar;
ALTER TABLE entity_one ADD some_uuid uuid;
'''

    CassandraMigrationBuilder cassandraMigrationBuilder

    @Test
    void "path is built for existing entity in non-empty folder"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = new RefreshableTableImpl(EntityOne.class)
        refreshableTable.setFields getEntityFields()
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        Assert.assertEquals TEST_MIGRATION_DIRECTORY + "github_social_stats/V2_0__Update_entity_one_table.cql",
                cassandraBuiltFile.getPath()
    }

    @Test
    void "path is build for existing entity in empty folder"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(EMPTY_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = new RefreshableTableImpl(EntityOne.class)
        refreshableTable.setFields getEntityFields()
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        Assert.assertEquals EMPTY_MIGRATION_DIRECTORY + "github_social_stats/V1_0__Update_entity_one_table.cql",
                cassandraBuiltFile.getPath()
    }

    @Test
    void "path is built for empty entity in non-empty folder"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = new RefreshableTableImpl(EntityEmpty.class)
        refreshableTable.setFields getEntityFields()
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        Assert.assertEquals TEST_MIGRATION_DIRECTORY + "github_social_stats/V2_0__Create_entity_empty_table.cql",
                cassandraBuiltFile.getPath()
    }

    @Test
    void "path is built for empty entity in empty folder"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(EMPTY_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = new RefreshableTableImpl(EntityEmpty.class)
        refreshableTable.setFields getEntityFields()
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        Assert.assertEquals EMPTY_MIGRATION_DIRECTORY + "github_social_stats/V1_0__Create_entity_empty_table.cql",
                cassandraBuiltFile.getPath()
    }

    @Test
    void "CQL is built for new entity with new fields"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = new RefreshableTableImpl(EntityEmpty.class)
        refreshableTable.setFields getEntityFields()
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        Assert.assertEquals NEW_ENTITY_CONTENTS_WITH_NEW_FIELDS, cassandraBuiltFile.getContents()
    }

    @Test
    void "CQL is built for new entity without new fields"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = new RefreshableTableImpl(EntityEmpty.class)
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        Assert.assertEquals NEW_ENTITY_CONTENTS_WITHOUT_NEW_FIELDS, cassandraBuiltFile.getContents()
    }

    @Test
    void "CQL is built for existing entity"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = new RefreshableTableImpl(EntityOne.class)
        refreshableTable.setFields getEntityFields()
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        Assert.assertEquals EXISTING_ENTITY_CONTENTS, cassandraBuiltFile.getContents()
    }

    @Test
    void "null object is returned when no changes has been made to non-empty entity"() {
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = new RefreshableTableImpl(EntityOne.class)
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        Assert.assertTrue cassandraBuiltFile instanceof CassandraBuiltFileNullObject
    }

    private static TreeSet<EntityField> getEntityFields() {
        TreeSet<EntityField> entityFieldTreeMap = TreeSetEntityFieldFactory.create()

        entityFieldTreeMap.add new EntityField("language_100")
        entityFieldTreeMap.add new EntityField("some_uuid", UUID.class)
        entityFieldTreeMap.add new EntityField("some_string", String.class)

        return entityFieldTreeMap
    }

}

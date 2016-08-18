package com.cezarykluczynski.carmen.cron.languages.builder

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile
import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityEmpty
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import com.cezarykluczynski.carmen.cron.languages.model.CassandraBuiltFileNullObject
import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import com.cezarykluczynski.carmen.cron.languages.model.RefreshableTableImpl
import spock.lang.Specification

class CassandraMigrationBuilderTest extends Specification {

    private static final String TEST_MIGRATION_DIRECTORY =
            "src/test/groovy/com/cezarykluczynski/carmen/cron/languages/fixture/migration/"
    private static final String EMPTY_MIGRATION_DIRECTORY =
            "src/test/groovy/com/cezarykluczynski/carmen/cron/languages/fixture/migration/empty/"
    private static final String NEW_ENTITY_CONTENTS_WITHOUT_NEW_FIELDS = '''\
CREATE TABLE github_social_stats.entity_empty;
'''
    private static final String NEW_ENTITY_CONTENTS_WITH_NEW_FIELDS = '''\
CREATE TABLE github_social_stats.entity_empty;

ALTER TABLE github_social_stats.entity_empty ADD language_100 int;
ALTER TABLE github_social_stats.entity_empty ADD some_string varchar;
ALTER TABLE github_social_stats.entity_empty ADD some_uuid uuid;
'''
    private static final String EXISTING_ENTITY_CONTENTS = '''\
ALTER TABLE github_social_stats.entity_one ADD language_100 int;
ALTER TABLE github_social_stats.entity_one ADD some_string varchar;
ALTER TABLE github_social_stats.entity_one ADD some_uuid uuid;
'''

    private static final LanguagesIteratorsFactory languagesIteratorFactory = new LanguagesIteratorsFactory()

    private CassandraMigrationBuilder cassandraMigrationBuilder

    def "path is built for existing entity in non-empty folder"() {
        given:
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = createEntityFieldsIterator(EntityOne.class)
        refreshableTable.setFields getEntityFields()

        when:
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        then:
        TEST_MIGRATION_DIRECTORY + "github_social_stats/V2_0__Update_entity_one_table.cql" == cassandraBuiltFile.getPath()
    }

    def "path is build for existing entity in empty folder"() {
        given:
        cassandraMigrationBuilder = new CassandraMigrationBuilder(EMPTY_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = createEntityFieldsIterator(EntityOne.class)
        refreshableTable.setFields getEntityFields()

        when:
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        then:
        EMPTY_MIGRATION_DIRECTORY + "github_social_stats/V1_0__Update_entity_one_table.cql" == cassandraBuiltFile.getPath()
    }

    def "path is built for empty entity in non-empty folder"() {
        given:
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = createEntityFieldsIterator(EntityEmpty.class)
        refreshableTable.setFields getEntityFields()

        when:
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        then:
        TEST_MIGRATION_DIRECTORY + "github_social_stats/V2_0__Create_entity_empty_table.cql" == cassandraBuiltFile.getPath()
    }

    def "path is built for empty entity in empty folder"() {
        given:
        cassandraMigrationBuilder = new CassandraMigrationBuilder(EMPTY_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = createEntityFieldsIterator(EntityEmpty.class)
        refreshableTable.setFields getEntityFields()

        when:
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        then:
        EMPTY_MIGRATION_DIRECTORY + "github_social_stats/V1_0__Create_entity_empty_table.cql" == cassandraBuiltFile.getPath()
    }

    def "CQL is built for new entity with new fields"() {
        given:
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = createEntityFieldsIterator(EntityEmpty.class)
        refreshableTable.setFields getEntityFields()

        when:
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        then:
        NEW_ENTITY_CONTENTS_WITH_NEW_FIELDS == cassandraBuiltFile.getContents()
    }

    def "CQL is built for new entity without new fields"() {
        given:
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = createEntityFieldsIterator(EntityEmpty.class)

        when:
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        then:
        NEW_ENTITY_CONTENTS_WITHOUT_NEW_FIELDS == cassandraBuiltFile.getContents()
    }

    def "CQL is built for existing entity"() {
        given:
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = createEntityFieldsIterator(EntityOne.class)
        refreshableTable.setFields getEntityFields()

        when:
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        then:
        EXISTING_ENTITY_CONTENTS == cassandraBuiltFile.getContents()
    }

    def "null object is returned when no changes has been made to non-empty entity"() {
        given:
        cassandraMigrationBuilder = new CassandraMigrationBuilder(TEST_MIGRATION_DIRECTORY)
        RefreshableTable refreshableTable = createEntityFieldsIterator(EntityOne.class)

        when:
        CassandraBuiltFile cassandraBuiltFile = cassandraMigrationBuilder.build(refreshableTable)

        then:
        cassandraBuiltFile instanceof CassandraBuiltFileNullObject
    }

    private static TreeSet<EntityField> getEntityFields() {
        TreeSet<EntityField> entityFieldTreeMap = TreeSetEntityFieldFactory.create()

        entityFieldTreeMap.add new EntityField("language_100")
        entityFieldTreeMap.add new EntityField("some_uuid", UUID.class)
        entityFieldTreeMap.add new EntityField("some_string", String.class)

        return entityFieldTreeMap
    }

    private static RefreshableTableImpl createEntityFieldsIterator(Class clazz) {
        return new RefreshableTableImpl(clazz, languagesIteratorFactory
                .createEntityFieldsIterator(clazz, FieldsFilter.ALL))
    }

}

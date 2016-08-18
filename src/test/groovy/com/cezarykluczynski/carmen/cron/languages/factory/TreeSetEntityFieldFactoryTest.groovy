package com.cezarykluczynski.carmen.cron.languages.factory

import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import org.testng.Assert
import spock.lang.Specification

import java.time.LocalDateTime

public class TreeSetEntityFieldFactoryTest  extends Specification {

    private TreeSet<EntityField> entityFieldTreeSet

    def setup() {
        entityFieldTreeSet = TreeSetEntityFieldFactory.create()

        ArrayList<EntityField> entityFieldArrayList = new ArrayList<>()

        entityFieldArrayList.add new EntityField("id", UUID.class)
        entityFieldArrayList.add new EntityField("created", LocalDateTime.class)

        entityFieldArrayList.add new EntityField("language_1")
        entityFieldArrayList.add new EntityField("language_2")
        entityFieldArrayList.add new EntityField("language_3")
        entityFieldArrayList.add new EntityField("language_10")
        entityFieldArrayList.add new EntityField("language_30")
        entityFieldArrayList.add new EntityField("language_100")
        entityFieldArrayList.add new EntityField("language_300")
        entityFieldArrayList.add new EntityField("language_500")
        entityFieldArrayList.add new EntityField("language_1000")

        entityFieldArrayList.add new EntityField("language_1_added")
        entityFieldArrayList.add new EntityField("language_2_added")
        entityFieldArrayList.add new EntityField("language_3_added")
        entityFieldArrayList.add new EntityField("language_10_added")
        entityFieldArrayList.add new EntityField("language_30_added")
        entityFieldArrayList.add new EntityField("language_100_added")
        entityFieldArrayList.add new EntityField("language_300_added")
        entityFieldArrayList.add new EntityField("language_500_added")
        entityFieldArrayList.add new EntityField("language_1000_added")

        entityFieldArrayList.add new EntityField("language_1_removed")
        entityFieldArrayList.add new EntityField("language_2_removed")
        entityFieldArrayList.add new EntityField("language_3_removed")
        entityFieldArrayList.add new EntityField("language_10_removed")
        entityFieldArrayList.add new EntityField("language_30_removed")
        entityFieldArrayList.add new EntityField("language_100_removed")
        entityFieldArrayList.add new EntityField("language_300_removed")
        entityFieldArrayList.add new EntityField("language_500_removed")
        entityFieldArrayList.add new EntityField("language_1000_removed")

        entityFieldArrayList.add new EntityField("refreshed", LocalDateTime.class)

        Collections.shuffle(entityFieldArrayList)

        for(EntityField entityField : entityFieldArrayList) {
            entityFieldTreeSet.add entityField
        }
    }

    def "comparator produces correct order"() {
        when:
        Iterator<EntityField> iterator = entityFieldTreeSet.iterator()

        then:
        iterator.next().getName() == "created"
        iterator.next().getName() == "id"

        iterator.next().getName() == "language_1"
        iterator.next().getName() == "language_1_added"
        iterator.next().getName() == "language_1_removed"

        iterator.next().getName() == "language_2"
        iterator.next().getName() == "language_2_added"
        iterator.next().getName() == "language_2_removed"

        iterator.next().getName() == "language_3"
        iterator.next().getName() == "language_3_added"
        iterator.next().getName() == "language_3_removed"

        iterator.next().getName() == "language_10"
        iterator.next().getName() == "language_10_added"
        iterator.next().getName() == "language_10_removed"

        iterator.next().getName() == "language_30"
        iterator.next().getName() == "language_30_added"
        iterator.next().getName() == "language_30_removed"

        iterator.next().getName() == "language_100"
        iterator.next().getName() == "language_100_added"
        iterator.next().getName() == "language_100_removed"

        iterator.next().getName() == "language_300"
        iterator.next().getName() == "language_300_added"
        iterator.next().getName() == "language_300_removed"

        iterator.next().getName() == "language_500"
        iterator.next().getName() == "language_500_added"
        iterator.next().getName() == "language_500_removed"

        iterator.next().getName() == "language_1000"
        iterator.next().getName() == "language_1000_added"
        iterator.next().getName() == "language_1000_removed"

        iterator.next().getName() == "refreshed"

        Assert.assertFalse iterator.hasNext()
    }

    def "exact copy is created"() {
        when:
        TreeSet<EntityField> entityFieldTreeSetCopy = TreeSetEntityFieldFactory.copy entityFieldTreeSet

        Iterator<EntityField> iterator = entityFieldTreeSet.iterator()
        Iterator<EntityField> copyIterator = entityFieldTreeSetCopy.iterator()

        then:
        while(iterator.hasNext()) {
            assert iterator.next() == copyIterator.next()
        }
    }

}

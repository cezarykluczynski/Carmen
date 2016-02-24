package com.cezarykluczynski.carmen.cron.languages.factory

import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import java.time.LocalDateTime

public class TreeSetEntityFieldFactoryTest {

    TreeSet<EntityField> entityFieldTreeSet

    @BeforeMethod
    void setUp() {
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

    @Test
    void "comparator produces correct order"() {
        Iterator<EntityField> iterator = entityFieldTreeSet.iterator()

        Assert.assertEquals iterator.next().getName(), "created"
        Assert.assertEquals iterator.next().getName(), "id"

        Assert.assertEquals iterator.next().getName(), "language_1"
        Assert.assertEquals iterator.next().getName(), "language_1_added"
        Assert.assertEquals iterator.next().getName(), "language_1_removed"

        Assert.assertEquals iterator.next().getName(), "language_2"
        Assert.assertEquals iterator.next().getName(), "language_2_added"
        Assert.assertEquals iterator.next().getName(), "language_2_removed"

        Assert.assertEquals iterator.next().getName(), "language_3"
        Assert.assertEquals iterator.next().getName(), "language_3_added"
        Assert.assertEquals iterator.next().getName(), "language_3_removed"

        Assert.assertEquals iterator.next().getName(), "language_10"
        Assert.assertEquals iterator.next().getName(), "language_10_added"
        Assert.assertEquals iterator.next().getName(), "language_10_removed"

        Assert.assertEquals iterator.next().getName(), "language_30"
        Assert.assertEquals iterator.next().getName(), "language_30_added"
        Assert.assertEquals iterator.next().getName(), "language_30_removed"

        Assert.assertEquals iterator.next().getName(), "language_100"
        Assert.assertEquals iterator.next().getName(), "language_100_added"
        Assert.assertEquals iterator.next().getName(), "language_100_removed"

        Assert.assertEquals iterator.next().getName(), "language_300"
        Assert.assertEquals iterator.next().getName(), "language_300_added"
        Assert.assertEquals iterator.next().getName(), "language_300_removed"

        Assert.assertEquals iterator.next().getName(), "language_500"
        Assert.assertEquals iterator.next().getName(), "language_500_added"
        Assert.assertEquals iterator.next().getName(), "language_500_removed"

        Assert.assertEquals iterator.next().getName(), "language_1000"
        Assert.assertEquals iterator.next().getName(), "language_1000_added"
        Assert.assertEquals iterator.next().getName(), "language_1000_removed"

        Assert.assertEquals iterator.next().getName(), "refreshed"

        Assert.assertFalse iterator.hasNext()
    }

    @Test
    void "exact copy is created"() {
        TreeSet<EntityField> entityFieldTreeSetCopy = TreeSetEntityFieldFactory.copy entityFieldTreeSet

        Iterator<EntityField> iterator = entityFieldTreeSet.iterator()
        Iterator<EntityField> copyIterator = entityFieldTreeSetCopy.iterator()

        while(iterator.hasNext()) {
            Assert.assertEquals iterator.next(), copyIterator.next()
        }
    }

}

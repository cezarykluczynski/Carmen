package com.cezarykluczynski.carmen.cron.management.converter

import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO
import com.cezarykluczynski.carmen.model.pub.Cron
import org.testng.Assert
import org.testng.annotations.Test

class DatabaseSwitchableJobDTOConverterTest {

    private static final String NAME = "Some cron"
    private static final boolean ENABLED = true

    @Test
    void "converts null to null"() {
        Assert.assertNull DatabaseSwitchableJobDTOConverter.fromEntity(null)
    }

    @Test
    void "converts from entity"() {
        // setup
        Cron entity = new Cron()
        entity.setName NAME
        entity.setEnabled ENABLED

        // exercise
        DatabaseSwitchableJobDTO dto = DatabaseSwitchableJobDTOConverter.fromEntity entity

        // assertion
        dto.name == NAME
        dto.enabled == ENABLED
    }

}

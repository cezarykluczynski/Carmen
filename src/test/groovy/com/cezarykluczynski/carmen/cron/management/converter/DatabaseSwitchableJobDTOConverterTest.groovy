package com.cezarykluczynski.carmen.cron.management.converter

import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO
import com.cezarykluczynski.carmen.model.pub.Cron
import spock.lang.Specification

class DatabaseSwitchableJobDTOConverterTest extends Specification {

    private static final String NAME = "Some cron"
    private static final boolean ENABLED = true

    def "converts null to null"() {
        expect:
        DatabaseSwitchableJobDTOConverter.fromEntity(null) == null
    }

    def "converts from entity"() {
        given:
        Cron entity = new Cron(
                name: NAME,
                enabled: ENABLED
        )

        when:
        DatabaseSwitchableJobDTO dto = DatabaseSwitchableJobDTOConverter.fromEntity entity

        then:
        dto.name == NAME
        dto.enabled == ENABLED
    }

}

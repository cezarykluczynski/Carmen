package com.cezarykluczynski.carmen.cron.management.converter;

import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO;
import com.cezarykluczynski.carmen.cron.model.entity.Cron;

public class DatabaseSwitchableJobDTOConverter {

    public static DatabaseSwitchableJobDTO fromEntity(Cron cron) {
        if (cron == null) {
            return null;
        }

        return DatabaseSwitchableJobDTO.builder()
                .name(cron.getName())
                .enabled(cron.isEnabled())
                .build();
    }

}

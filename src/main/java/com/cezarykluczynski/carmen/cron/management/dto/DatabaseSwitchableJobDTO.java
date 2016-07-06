package com.cezarykluczynski.carmen.cron.management.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DatabaseSwitchableJobDTO {

    private String name;

    private boolean enabled;

}

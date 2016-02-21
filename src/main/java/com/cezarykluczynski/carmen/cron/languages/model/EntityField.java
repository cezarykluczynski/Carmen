package com.cezarykluczynski.carmen.cron.languages.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class EntityField {

    @Getter
    private String name;

    @Getter
    private Class type;

    public EntityField(String name) {
        this.name = name;
        this.type = Integer.class;
    }

    public EntityField(String name, Class type) {
        this.name = name;
        this.type = type;
    }

}

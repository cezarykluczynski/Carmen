package com.cezarykluczynski.carmen.data.language.model.entity.enums;

public enum LinguistLanguageType {

    DATA("data"),
    PROGRAMMING("programming"),
    MARKUP("markup"),
    PROSE("prose"),
    NONE("none");

    private String value;

    LinguistLanguageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

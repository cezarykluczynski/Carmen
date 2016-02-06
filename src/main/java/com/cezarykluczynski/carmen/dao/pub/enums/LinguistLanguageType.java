package com.cezarykluczynski.carmen.dao.pub.enums;

public enum LinguistLanguageType {

    DATA("data"),
    PROGRAMMING("programming"),
    MARKUP("markup"),
    PROSE("prose"),
    NONE("none");

    protected String value;

    private LinguistLanguageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

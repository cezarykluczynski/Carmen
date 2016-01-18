package com.cezarykluczynski.carmen.lang.stats.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Language {

    private String name;

    private LanguageType type;

    private String color;

    private Language parent;

    public Language(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(LanguageType type) {
        this.type = type;
    }

    public LanguageType getType() {
        return type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setParent(Language parent) {
        this.parent = parent;
    }

    public Language getParent() {
        return parent;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Language)) {
            return false;
        }

        Language language = (Language) o;

        if (!this.getName().equals(language.getName())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder().append(name).toHashCode();
    }

}

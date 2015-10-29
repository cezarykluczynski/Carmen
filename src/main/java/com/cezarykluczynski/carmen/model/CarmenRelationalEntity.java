package com.cezarykluczynski.carmen.model;

public abstract class CarmenRelationalEntity extends CarmenEntity {

    public abstract Long getId();

    public boolean equals(Object objectToCompare) {
        if (objectToCompare == this) {
            return true;
        }

        if (!this.getClass().isInstance(objectToCompare)) {
            return false;
        }

        CarmenRelationalEntity entityToCompare = (CarmenRelationalEntity) objectToCompare;

        if (entityToCompare.getId().equals(getId())) {
            return true;
        }

        return false;
    }

}

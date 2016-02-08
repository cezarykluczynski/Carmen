package com.cezarykluczynski.carmen.rest.pojo.api.v1.github.error;

import javax.persistence.Entity;

@Entity
public class Error404ResponsePOJO {

    public Error404ResponsePOJO() {
    }

    private static final String message = "404 Not Found";

    public String getMessage() {
        return message;
    }

}

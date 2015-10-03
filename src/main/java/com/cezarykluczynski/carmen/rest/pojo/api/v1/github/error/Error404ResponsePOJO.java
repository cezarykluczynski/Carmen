package com.cezarykluczynski.carmen.pojo.rest.api.v1.github.error;

import javax.persistence.Entity;

@Entity
public class Error404ResponsePOJO {

    public Error404ResponsePOJO() {
    }

    private String message = "404 Not Found";

    public String getMessage() {
        return message;
    }

}

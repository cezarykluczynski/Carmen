package com.cezarykluczynski.carmen.set.github;

import javax.persistence.Entity;

import java.util.Date;

@Entity
public class RateLimit {

    public RateLimit(String resource, Integer limit, Integer remaining, Date reset) {
        this.resource = resource;
        this.limit = limit;
        this.remaining = remaining;
        this.reset = reset;
    }

    public String resource;
    public Integer limit;
    public Integer remaining;
    public Date reset;

    public String getResource() {
        return resource;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public Date getReset() {
        return reset;
    }

}

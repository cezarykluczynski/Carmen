package com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class RateLimitDTO {

    private String resource;

    private Integer limit;

    private Integer remaining;

    private Date reset;

}

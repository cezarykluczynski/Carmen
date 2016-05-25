package com.cezarykluczynski.carmen.admin.web.endpoint.dto;

import lombok.Builder;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Builder
@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LanguagesListUpdateStatusDTO {

    private Boolean updatable;

    private Integer persistedLanguagesCount;

    private Integer linguistLanguagesCount;

}

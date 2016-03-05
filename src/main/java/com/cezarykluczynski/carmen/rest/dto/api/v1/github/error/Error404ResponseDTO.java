package com.cezarykluczynski.carmen.rest.dto.api.v1.github.error;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class Error404ResponseDTO {

    private String message = "404 Not Found";

}

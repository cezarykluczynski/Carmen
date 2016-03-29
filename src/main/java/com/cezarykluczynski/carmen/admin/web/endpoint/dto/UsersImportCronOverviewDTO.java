package com.cezarykluczynski.carmen.admin.web.endpoint.dto;

import lombok.Builder;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Builder
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class UsersImportCronOverviewDTO {

    private Integer highestGitHubUserId;

    private boolean enabled;

    private boolean running;

}

package com.cezarykluczynski.carmen.admin.web.endpoint.dto;

import com.cezarykluczynski.carmen.rest.serializer.LocalDateTimeDeserializer;
import com.cezarykluczynski.carmen.rest.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@Builder
@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SchemaUpdateStatusDTO {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updated;

    private String linguistVersion;

    private Boolean enabled;

    private Boolean running;

    private Boolean saved;

    private Boolean executed;

}

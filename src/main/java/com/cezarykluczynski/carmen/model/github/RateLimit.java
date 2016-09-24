package com.cezarykluczynski.carmen.model.github;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import java.util.Date;

@Data
@Entity(name = "github.RateLimit")
@Table(schema = "github", name = "rate_limits")
@EqualsAndHashCode(callSuper = false)
public class RateLimit extends CarmenRelationalEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String resource;

    @Column(name = "resource_limit")
    private Integer limit;

    private Integer remaining;

    @Column(name = "resource_reset")
    private Date reset;

    private Date updated;

}

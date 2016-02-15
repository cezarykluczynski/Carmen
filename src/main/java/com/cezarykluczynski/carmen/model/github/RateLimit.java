package com.cezarykluczynski.carmen.model.github;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;

import javax.persistence.*;

import java.util.Date;

@Data
@Entity(name = "github.RateLimit")
@Table(schema = "github", name = "rate_limits")
public class RateLimit extends CarmenRelationalEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String resource;

    @Column(name = "resource_limit")
    private Integer limit;

    @Column
    private Integer remaining;

    @Column(name = "resource_reset")
    private Date reset;

    @Column
    private Date updated;

}

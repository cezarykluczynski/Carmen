package com.cezarykluczynski.carmen.cron.model.entity;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "public.Cron")
@Table(schema = "public", name = "crons")
@EqualsAndHashCode(callSuper = false)
public class Cron extends CarmenRelationalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String server;

    private boolean enabled;

    private boolean running;

    private Date updated;

}

package com.cezarykluczynski.carmen.model.pub;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "public.Cron")
@Table(schema = "public", name = "crons")
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

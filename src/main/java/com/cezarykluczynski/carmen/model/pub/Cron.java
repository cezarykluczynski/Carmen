package com.cezarykluczynski.carmen.model.pub;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "public.Cron")
@Table(schema = "public", name = "crons")
public class Cron extends CarmenRelationalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String name;

    @Column
    private String server;

    @Column
    private boolean enabled;

    @Column
    private boolean running;

    @Column
    private LocalDateTime updated;

}

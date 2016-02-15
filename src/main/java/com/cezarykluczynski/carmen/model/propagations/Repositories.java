package com.cezarykluczynski.carmen.model.propagations;

import javax.persistence.*;

import java.util.Date;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import com.cezarykluczynski.carmen.model.github.User;
import lombok.Data;

@Data
@Entity(name = "propagations.Repositories")
@Table(schema = "propagations", name = "repositories")
public class Repositories extends CarmenRelationalEntity implements Propagation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "github_user_id", referencedColumnName = "id")
    private User user;

    @Column
    private String phase;

    @Column
    private Integer page;

    @Column
    private Date updated;

}

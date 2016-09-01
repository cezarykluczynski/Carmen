package com.cezarykluczynski.carmen.model.github;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "github.RepositoryClone")
@Table(schema = "github", name = "repositories_clones")
public class RepositoryClone extends CarmenRelationalEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="github_repository_id", referencedColumnName = "id", unique = true, nullable = false)
    private Repository repository;

    @OneToOne
    @JoinColumn(name="parent_id")
    private RepositoryClone parent;

    private String locationDirectory;

    private String locationSubdirectory;

    private String serverId;

    private Date cloned;

    private Date updated;

    private Date commitsStatisticsUntil;

}

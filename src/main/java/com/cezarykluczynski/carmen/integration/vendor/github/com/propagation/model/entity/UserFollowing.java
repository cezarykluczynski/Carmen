package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "propagations.UserFollowing")
@Table(schema = "propagations", name = "user_following")
@ToString(exclude = {"user"})
public class UserFollowing extends CarmenRelationalEntity implements Propagation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "github_user_id", referencedColumnName = "id")
    private User user;

    private String phase;

    private Integer page;

    private Date updated;

}

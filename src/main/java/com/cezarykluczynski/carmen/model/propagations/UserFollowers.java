package com.cezarykluczynski.carmen.model.propagations;

import javax.persistence.*;

import java.util.Date;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import com.cezarykluczynski.carmen.model.github.User;
import lombok.Data;
import lombok.ToString;

@Data
@Entity(name = "propagations.UserFollowers")
@Table(schema = "propagations", name = "user_followers")
@ToString(exclude = {"user"})
public class UserFollowers extends CarmenRelationalEntity implements Propagation {

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

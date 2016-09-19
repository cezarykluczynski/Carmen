package com.cezarykluczynski.carmen.common.user.model.entity;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "users.User")
@Table(schema = "users", name = "users")
public class CommonUser extends CarmenRelationalEntity {

    @Id
    @GeneratedValue
    private Long id;

}

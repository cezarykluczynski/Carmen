package com.cezarykluczynski.carmen.model.users;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "users.User")
@Table(schema = "users", name = "users")
public class User extends CarmenRelationalEntity {

    @Id
    @GeneratedValue
    private Long id;

}

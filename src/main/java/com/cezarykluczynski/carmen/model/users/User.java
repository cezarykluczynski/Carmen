package com.cezarykluczynski.carmen.model.users;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;

import javax.persistence.*;

@Entity(name = "users.User")
@Table(schema = "users", name = "users")
public class User extends CarmenRelationalEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

}

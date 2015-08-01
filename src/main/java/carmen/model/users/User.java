package carmen.model.users;

import javax.persistence.*;

@Entity(name = "users.User")
@Table(schema = "users", name = "users")
public class User {

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
package carmen.model;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "users")
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
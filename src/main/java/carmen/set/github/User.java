package carmen.set.github;

import javax.persistence.Entity;

import java.util.Map;

@Entity
public class User {

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int id;
    public String username;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean exists() {
        return id > 0;
    }
}
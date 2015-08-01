package carmen.set.github;

import javax.persistence.Entity;

@Entity
public class User {

    public User(Long id, String login, String name) {
        this.id = id;
        this.login = login;
        this.name = name;
    }

    public User(Object id, String login) {
        this.id = null;
        this.login = login;
        this.name = "";
    }

    public Long id;
    public String login;
    public String name;

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public boolean exists() {
        return id != null;
    }
}
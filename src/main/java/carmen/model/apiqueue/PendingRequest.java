package carmen.model.apiqueue;

import javax.persistence.*;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import carmen.model.github.User;

@Entity(name = "api_queue.PendingRequests")
@Table(schema = "api_queue", name = "pending_requests")
public class PendingRequest {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "github_user_id")
    private User user;

    @Column
    private String executor;

    @Column(name = "path_params")
    private String pathParams;

    @Column(name = "query_params")
    private String queryParams;

    @Column
    private Integer priority;

    @Column
    private Date updated;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getExecutor() {
        return executor;
    }

    public void setPathParams(HashMap<String, Object> pathParams) {
        this.pathParams = new JSONObject(pathParams).toString();
    }

    public String getPathParams() {
        return pathParams;
    }

    public void setQueryParams(HashMap<String, Object> queryParams) {
        this.queryParams = new JSONObject(queryParams).toString();
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setUpdated() {
        updated = new Date();
    }

    public Date getUpdated() {
        return updated;
    }
}

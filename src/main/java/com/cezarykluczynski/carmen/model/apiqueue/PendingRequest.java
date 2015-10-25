package com.cezarykluczynski.carmen.model.apiqueue;

import javax.persistence.*;

import java.util.Date;
import java.util.HashMap;
import org.json.JSONObject;

import com.cezarykluczynski.carmen.util.HashMapJsonConverter;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.propagations.Propagation;

@Entity(name = "api_queue.PendingRequests")
@Table(schema = "api_queue", name = "pending_requests")
public class PendingRequest {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "github_user_id", referencedColumnName = "id")
    private User user;

    @Column
    private String executor;

    @Column(name = "params")
    private String params;

    @Column(name = "path_params")
    private String pathParams;

    @Column(name = "query_params")
    private String queryParams;

    @Column(name = "propagation_id")
    private Long propagationId;

    @Column
    private Integer priority;

    @Column
    private Date updated;

    public Long getId() {
        return id;
    }

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

    public void setParams(HashMap<String, Object> params) {
        this.params = HashMapJsonConverter.hashMapToJsonString(params);
    }

    public HashMap<String, Object> getParams() {
        return HashMapJsonConverter.jsonStringToHashMap(params);
    }

    public void setPathParams(HashMap<String, Object> pathParams) {
        this.pathParams = HashMapJsonConverter.hashMapToJsonString(pathParams);
    }

    public HashMap<String, Object> getPathParams() {
        return HashMapJsonConverter.jsonStringToHashMap(pathParams);
    }

    public void setQueryParams(HashMap<String, Object> queryParams) {
        this.queryParams = HashMapJsonConverter.hashMapToJsonString(queryParams);
    }

    public HashMap<String, Object> getQueryParams() {
        return HashMapJsonConverter.jsonStringToHashMap(queryParams);
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public Long getPropagationId() {
        return propagationId;
    }

    public void setPropagation(Propagation propagation) {
        propagationId = propagation.getId();
    }

    public void setPropagationId(Long propagationId) {
        this.propagationId = propagationId;
    }

    public void setUpdated() {
        updated = new Date();
    }

    public Date getUpdated() {
        return updated;
    }
}

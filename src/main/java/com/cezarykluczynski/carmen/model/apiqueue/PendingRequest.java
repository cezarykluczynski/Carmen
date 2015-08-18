package com.cezarykluczynski.carmen.model.apiqueue;

import javax.persistence.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

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
    @JoinColumn(name = "github_user_id")
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
        this.params = new JSONObject(params).toString();
    }

    public HashMap<String, Object> getParams() {
        return jsonToHashMap(params);
    }

    public void setPathParams(HashMap<String, Object> pathParams) {
        this.pathParams = new JSONObject(pathParams).toString();
    }

    public HashMap<String, Object> getPathParams() {
        return jsonToHashMap(pathParams);
    }

    public void setQueryParams(HashMap<String, Object> queryParams) {
        this.queryParams = new JSONObject(queryParams).toString();
    }

    public HashMap<String, Object> getQueryParams() {
        return jsonToHashMap(queryParams);
    }

    // TODO: move to utils
    private HashMap<String, Object> jsonToHashMap(String jsonString) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        JSONObject json = new JSONObject(jsonString);
        Iterator keys = json.keys();
        String key;

        while (keys.hasNext()) {
            key = (String) keys.next();
            hashMap.put(key, json.get(key));
        }

        return hashMap;
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

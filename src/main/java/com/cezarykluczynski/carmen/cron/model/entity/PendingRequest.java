package com.cezarykluczynski.carmen.cron.model.entity;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import com.cezarykluczynski.carmen.util.HashMapJsonConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;

@Entity(name = "api_queue.PendingRequests")
@Table(schema = "api_queue", name = "pending_requests")
public class PendingRequest extends CarmenRelationalEntity {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "github_user_id", referencedColumnName = "id")
    private User user;

    @Getter
    @Setter
    private String executor;

    private String params;

    private String pathParams;

    private String queryParams;

    @Getter
    @Setter
    private Long propagationId;

    @Getter
    @Setter
    private Integer priority;

    @Getter
    @Setter
    private Date updated;


    public void setParams(HashMap<String, Object> params) {
        this.params = HashMapJsonConverter.hashMapToJsonString(params);
    }

    public HashMap<String, Object> getParams() {
        HashMap<String, Object> paramsMap = HashMapJsonConverter.jsonStringToHashMap(params);
        if (paramsMap.containsKey("link_with")) {
            paramsMap.put("link_with", new Long((int) paramsMap.get("link_with")));
        }
        return paramsMap;
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

}

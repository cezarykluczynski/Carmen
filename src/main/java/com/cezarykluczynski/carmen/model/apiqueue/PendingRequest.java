package com.cezarykluczynski.carmen.model.apiqueue;

import javax.persistence.*;

import java.util.Date;
import java.util.HashMap;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import com.cezarykluczynski.carmen.util.HashMapJsonConverter;
import com.cezarykluczynski.carmen.model.github.User;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "api_queue.PendingRequests")
@Table(schema = "api_queue", name = "pending_requests")
public class PendingRequest extends CarmenRelationalEntity {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "github_user_id", referencedColumnName = "id")
    private User user;

    @Getter
    @Setter
    @Column
    private String executor;

    @Column(name = "params")
    private String params;

    @Column(name = "path_params")
    private String pathParams;

    @Column(name = "query_params")
    private String queryParams;

    @Getter
    @Setter
    @Column(name = "propagation_id")
    private Long propagationId;

    @Getter
    @Setter
    @Column
    private Integer priority;

    @Getter
    @Setter
    @Column
    private Date updated;


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

}

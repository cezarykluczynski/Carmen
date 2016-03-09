package com.cezarykluczynski.carmen.vcs.server;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TomcatServer extends AbstractServer implements Server {

    private String serverId;

    private String cloneRoot;

    @Autowired
    public TomcatServer(
        @Value("${githubCloneTomcatServer.serverId}") String serverId,
        @Value("${githubCloneTomcatServer.cloneRoot}") String cloneRoot
    ) {
        Preconditions.checkNotNull(serverId);
        Preconditions.checkNotNull(cloneRoot);
        this.serverId = serverId;
        this.cloneRoot = cloneRoot;
    }

    public String getServerId() {
        return serverId;
    }

    public String getCloneRoot() {
        return cloneRoot;
    }

}

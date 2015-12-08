package com.cezarykluczynski.carmen.vcs.server;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TomcatServer extends AbstractServer {

    @Value("${githubCloneTomcatServer.serverId}")
    private String serverId;

    @Value("${githubCloneTomcatServer.cloneRoot}")
    private String cloneRoot;

    public TomcatServer() {
        Preconditions.checkNotNull(serverId);
        Preconditions.checkNotNull(cloneRoot);
    }

    public String getServerId() {
        return serverId;
    }

    public String getCloneRoot() {
        return cloneRoot;
    }

}

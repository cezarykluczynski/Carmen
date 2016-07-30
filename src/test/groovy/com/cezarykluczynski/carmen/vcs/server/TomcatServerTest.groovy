package com.cezarykluczynski.carmen.vcs.server

import spock.lang.Specification

class TomcatServerTest extends Specification {

    private static final String SERVER_ID = "SERVER_ID"
    private static final String CLONE_ROOT = "CLONE_ROOT"

    private TomcatServer tomcatServer

    def "does not allow null serverId"() {
        when:
        tomcatServer = new TomcatServer(null, CLONE_ROOT)

        then:
        thrown NullPointerException
    }

    def "does not allow null cloneRoot"() {
        when:
        tomcatServer = new TomcatServer(SERVER_ID, null)

        then:
        thrown NullPointerException
    }

    def "sets serverId and cloneRoot"() {
        when:
        tomcatServer = new TomcatServer(SERVER_ID, CLONE_ROOT)

        then:
        tomcatServer.serverId == SERVER_ID
        tomcatServer.cloneRoot == CLONE_ROOT
    }

}

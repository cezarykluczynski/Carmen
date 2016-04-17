package com.cezarykluczynski.carmen.test

import org.glassfish.jersey.test.JerseyTestNg
import org.glassfish.jersey.test.TestProperties

import javax.ws.rs.core.Application

public abstract class AbstractContainerPerClassTest extends JerseyTestNg.ContainerPerClassTest {

    @Override
    protected Application configure() {
        enable TestProperties.LOG_TRAFFIC
        enable TestProperties.CONTAINER_FACTORY
        enable TestProperties.DUMP_ENTITY
    }

}

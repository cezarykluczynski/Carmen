package com.cezarykluczynski.carmen.cron.github

import spock.lang.Specification

class CarmenNoopTaskExecutorTest extends Specification {

    private CarmenNoopTaskExecutor executor

    private Runnable runnable

    def setup() {
        executor = new CarmenNoopTaskExecutor()
    }

    def "do not interact with Runnable"() {
        given:
        runnable = Mock Runnable

        when:
        executor.execute runnable

        then:
        0 * runnable.run()
    }


}
package com.cezarykluczynski.carmen.util.exec

import com.cezarykluczynski.carmen.util.exec.executor.Executor
import spock.lang.Specification

class MkDirCommandTest extends Specification {

    private static final String DIRECTORY = "./tmp/MkDirTest" + System.currentTimeMillis()

    def "should create directory"() {
        given:
        assert !new File(DIRECTORY).exists()

        expect:
        Executor.execute(new MkDirCommand(DIRECTORY)).successFul
        new File(DIRECTORY).exists()

        cleanup:
        new File(DIRECTORY).delete()
    }

}
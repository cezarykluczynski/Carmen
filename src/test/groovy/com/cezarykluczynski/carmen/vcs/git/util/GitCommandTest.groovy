package com.cezarykluczynski.carmen.vcs.git.util

import com.cezarykluczynski.carmen.util.exec.executor.Executor
import com.cezarykluczynski.carmen.util.exec.result.Result
import spock.lang.Specification

class GitCommandTest extends Specification {

    def "git is available to Java"() {
        when:
        Result result = Executor.execute(new GitCommand("help"))

        then:
        result.isSuccessFul()
    }

    void "git version is sufficient"() {
        when:
        Result result = Executor.execute(new GitCommand("--version"))
        String output = result.getOutput()
        Integer major = Integer.parseInt(String.valueOf(output.charAt(12)))
        Integer minor = Integer.parseInt(String.valueOf(output.charAt(14)))

        then:
        result.isSuccessFul()
        output.contains("git version")
        major > 1 || minor >= 7
    }

}

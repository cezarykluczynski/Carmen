package com.cezarykluczynski.carmen.vcs.git.command

import com.cezarykluczynski.carmen.util.exec.executor.Executor
import com.cezarykluczynski.carmen.util.exec.result.Result
import spock.lang.Specification

class GitLogSinceCommandTest extends Specification {

    def "gets commits with since and before dates"() {
        when:
        Result result = Executor.execute(new GitLogSinceCommand(".", new Date(116, 4, 20, 12, 00, 00), new Date(116, 6, 20, 12, 00, 00)))
        String output = result.getOutput()

        then:
        result.isSuccessFul()
        output.contains("772352101f2496acf06c45bedb604add3653545b,1463774398,cezary.kluczynski@gmail.com")
        output.contains("e8b060049a144533ee01f788c00ac75804e2a729,1464290661,cezary.kluczynski@gmail.com")
    }

    def "gets commits with since date"() {
        when:
        Result result = Executor.execute(new GitLogSinceCommand(".", new Date(116, 4, 20, 12, 00, 00), null))
        String output = result.getOutput()

        then:
        result.isSuccessFul()
        output.contains("772352101f2496acf06c45bedb604add3653545b,1463774398,cezary.kluczynski@gmail.com")
        output.contains("e8b060049a144533ee01f788c00ac75804e2a729,1464290661,cezary.kluczynski@gmail.com")
    }

    def "gets commits with only before date"() {
        when:
        Result result = Executor.execute(new GitLogSinceCommand(".", null, new Date(115, 6, 26, 13, 48, 46)))
        String output = result.getOutput()

        then:
        result.isSuccessFul()
        output.contains("8812bf54bae631b0e8c1f204c29b03db4b28b971,1437838923,cezary.kluczynski@gmail.com")
        !output.contains("e8cbb02449e89bae71d7292fbebebcb2162a2e3c,1438104329,cezary.kluczynski@gmail.com")
    }

}

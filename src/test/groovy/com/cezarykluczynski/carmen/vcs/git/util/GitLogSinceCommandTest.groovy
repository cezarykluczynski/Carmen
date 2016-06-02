package com.cezarykluczynski.carmen.vcs.git.util

import com.cezarykluczynski.carmen.util.exec.Executor
import com.cezarykluczynski.carmen.util.exec.Result
import org.testng.Assert
import org.testng.annotations.Test

class GitLogSinceCommandTest {

    @Test
    void "gets commits"() {
        Result result = Executor.execute(new GitLogSinceCommand(".", new Date(116, 4, 20, 12, 00, 00)))

        println "get commits start"
        println result.getOutput()
        println result.getErrorReason()
        println result.getErrorMessage()
        println "get commits stop"

        Assert.assertTrue result.isSuccessFul()

        String output = result.getOutput()

        Assert.assertTrue output.contains("772352101f2496acf06c45bedb604add3653545b,1463774398,cezary.kluczynski@gmail.com")
        Assert.assertTrue output.contains("e8b060049a144533ee01f788c00ac75804e2a729,1464290661,cezary.kluczynski@gmail.com")
    }
}

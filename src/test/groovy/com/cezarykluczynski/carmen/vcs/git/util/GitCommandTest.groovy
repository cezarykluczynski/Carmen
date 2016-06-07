package com.cezarykluczynski.carmen.vcs.git.util

import com.cezarykluczynski.carmen.util.exec.executor.Executor
import com.cezarykluczynski.carmen.util.exec.result.Result
import org.testng.annotations.Test
import org.testng.Assert

class GitCommandTest {

    @Test
    void gitIsAvailableToJava() {
        // setup, exercise
        Result result = Executor.execute(new GitCommand("help"))

        // assertion
        Assert.assertTrue result.isSuccessFul()
    }

    @Test
    void gitVersionIsSufficient() {
        // setup, exercise
        Result result = Executor.execute(new GitCommand("--version"))

        // assertion
        String output = result.getOutput()
        Assert.assertTrue result.isSuccessFul()
        Assert.assertTrue output.contains("git version")
        Integer major = Integer.parseInt(String.valueOf(output.charAt(12)))
        Integer minor = Integer.parseInt(String.valueOf(output.charAt(14)))
        Assert.assertTrue(major >= 1)
        if (major == 1) {
            Assert.assertTrue(minor >= 7)
        }
    }

}

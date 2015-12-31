package com.cezarykluczynski.carmen.vcs.git.util

import com.cezarykluczynski.carmen.util.exec.Executor
import com.cezarykluczynski.carmen.util.exec.Result
import org.testng.annotations.Test
import org.testng.Assert

class GitCommandTest {

    @Test
    void gitIsAvailableToJava() {
        // setup, exercise
        Result result = Executor.execute(new GitCommand("help"))

        // assertion
        Assert.assertTrue result.isSuccessFull()
    }

    @Test
    void gitVersionIsSufficient() {
        // setup, exercise
        Result result = Executor.execute(new GitCommand("--version"))

        // assertion
        String output = result.getOutput()
        Assert.assertTrue result.isSuccessFull()
        Assert.assertTrue output.contains("git version")
        Assert.assertTrue(Integer.parseInt(String.valueOf(output.charAt(12))) >= 2)
        Assert.assertTrue(Integer.parseInt(String.valueOf(output.charAt(14))) >= 5)
    }

}

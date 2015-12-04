package com.cezarykluczynski.carmen.git.utils.command

import org.testng.annotations.Test
import org.testng.Assert

class ExecutorTest {

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
        Assert.assertEquals(output.charAt(12), (char) "2")
    }

}

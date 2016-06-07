package com.cezarykluczynski.carmen.vcs.git

import com.beust.jcommander.internal.Lists
import com.cezarykluczynski.carmen.util.exec.result.Result
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class GitLocalTest {

    private static final String DIFF_TARGET = "src/test/groovy/com/cezarykluczynski/carmen/fixture/git/diff-target"

    private static final String CHANGE_TEXT = "change"

    @BeforeMethod
    @AfterMethod
    void setUpTearDown() {
        File file = new File(DIFF_TARGET)
        file.write ""
    }

    @Test
    void "detects changed file"() {
        new File(DIFF_TARGET).write CHANGE_TEXT
        Result diffResult = GitLocal.diff Lists.newArrayList(DIFF_TARGET)
        Assert.assertTrue diffResult.isSuccessFul()
        Assert.assertTrue diffResult.getOutput().contains(CHANGE_TEXT)
    }

    @Test
    void "detects unchanged file"() {
        Result diffResult = GitLocal.diff Lists.newArrayList(DIFF_TARGET)
        Assert.assertTrue diffResult.isSuccessFul()
        Assert.assertEquals diffResult.getOutput().length(), 0
    }

}

package com.cezarykluczynski.carmen.vcs.git.util

import org.testng.Assert
import org.testng.annotations.Test

class DirectoryNameGeneratorTest {

    @Test
    void generateLocationDirectory() {
        String directoryName = DirectoryNameGenerator.generateLocationDirectory "cezarykluczynski/Carmen"

        Assert.assertEquals directoryName, "0/0c"
    }

}

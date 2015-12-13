package com.cezarykluczynski.carmen.util.filesystem

import org.testng.Assert
import org.testng.annotations.Test

class DirectoryTest {

    @Test
    void convertPathToUnixStyleSlashes() {
        Assert.assertEquals Directory.convertPathToUnixStyleSlashes("..\\a\\b\\c"), "../a/b/c"
        Assert.assertEquals Directory.convertPathToUnixStyleSlashes("..\\\\a\\b"), "..//a/b"
        Assert.assertEquals Directory.convertPathToUnixStyleSlashes("/a/b\\c"), "/a/b/c"
    }

}

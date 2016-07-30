package com.cezarykluczynski.carmen.util.filesystem

import spock.lang.Specification

class DirectoryTest extends Specification {

    def "converts path to unix style slashes"() {
        expect:
        Directory.convertPathToUnixStyleSlashes("..\\a\\b\\c") == "../a/b/c"
        Directory.convertPathToUnixStyleSlashes("..\\\\a\\b") == "..//a/b"
        Directory.convertPathToUnixStyleSlashes("/a/b\\c") == "/a/b/c"
    }

}

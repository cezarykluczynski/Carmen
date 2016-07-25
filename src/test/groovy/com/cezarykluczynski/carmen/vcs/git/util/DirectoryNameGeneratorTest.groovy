package com.cezarykluczynski.carmen.vcs.git.util

import spock.lang.Specification

class DirectoryNameGeneratorTest extends Specification {

    def "generates location directory"() {
        when:
        String directoryName = DirectoryNameGenerator.generateLocationDirectory "cezarykluczynski/Carmen"

        then:
        directoryName == "0/0c"
    }

}

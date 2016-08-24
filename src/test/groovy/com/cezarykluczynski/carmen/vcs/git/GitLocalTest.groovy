package com.cezarykluczynski.carmen.vcs.git

import com.cezarykluczynski.carmen.util.exec.result.Result
import com.google.common.collect.Lists
import spock.lang.Specification

class GitLocalTest extends Specification {

    private static final String DIFF_TARGET = "src/test/groovy/com/cezarykluczynski/carmen/fixture/git/diff-target"
    private static final String CHANGE_TEXT = "change"

    void setup() {
        clear()
    }

    void cleanup() {
        clear()
    }

    void "detects changed file"() {
        when:
        new File(DIFF_TARGET).write CHANGE_TEXT
        Result diffResult = GitLocal.diff Lists.newArrayList(DIFF_TARGET)

        then:
        diffResult.isSuccessFul()
        diffResult.getOutput().contains(CHANGE_TEXT)
    }

    void "detects unchanged file"() {
        when:
        Result diffResult = GitLocal.diff Lists.newArrayList(DIFF_TARGET)

        then:
        diffResult.isSuccessFul()
        diffResult.getOutput().length() == 0
    }

    private static clear() {
        File file = new File(DIFF_TARGET)
        file.write ""
    }

}

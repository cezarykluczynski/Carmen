package com.cezarykluczynski.carmen.vcs.git.command

import com.cezarykluczynski.carmen.util.exec.executor.Executor
import org.apache.commons.io.FileUtils
import spock.lang.Specification

class GitCloneCommandTest extends Specification {

    private static final String CLONE_URL = "tmp/GitCloneCommandTest/" + System.currentTimeMillis()
    private static final String ORIGIN = "new_origin"

    def "clones with custom origin"() {
        given:
        FileUtils.deleteDirectory new File(CLONE_URL)

        when:
        GitCloneCommand gitCloneCommand = new GitCloneCommand(".", CLONE_URL, ORIGIN)
        Executor.execute gitCloneCommand
        File f = new File("${CLONE_URL}/.git/refs/remotes/${ORIGIN}/HEAD");

        then:
        f.exists()

        cleanup:
        FileUtils.deleteQuietly new File(CLONE_URL)
    }

}
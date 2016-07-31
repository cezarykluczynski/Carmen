package com.cezarykluczynski.carmen.lang.stats.util

import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand
import com.cezarykluczynski.carmen.util.exec.command.Command
import com.cezarykluczynski.carmen.util.exec.executor.Executor
import com.cezarykluczynski.carmen.util.exec.result.Result
import spock.lang.Requires
import spock.lang.Specification

class LanguageDetectorServerSwitcherTest extends Specification {

    def "runs unsuccessfully without arguments"() {
        when:
        Command command = new ApacheCommonsCommand("java -cp target/classes com.cezarykluczynski.carmen" +
                ".lang.stats.util.LanguageDetectorServerSwitcher")
        Result result = Executor.execute(command)

        then:
        !result.successFul
    }

    def "runs unsuccessfully with more than one argument"() {
        when:
        Command command = new ApacheCommonsCommand("java -cp target/classes com.cezarykluczynski.carmen" +
                ".lang.stats.util.LanguageDetectorServerSwitcher \"one\" \"two\"")
        Result result = Executor.execute(command)

        then:
        !result.successFul
    }

    def "runs unsuccessfully when argument is neither \"start\" nor \"stop\""() {
        when:
        Command command = new ApacheCommonsCommand("java -cp target/classes com.cezarykluczynski.carmen" +
                ".lang.stats.util.LanguageDetectorServerSwitcher \"neither-stop-nor-start\"")
        Result result = Executor.execute(command)

        then:
        !result.successFul
    }

    @Requires({ LanguageDetectorServerSwitcher.getClient() == 'http' })
    def "starts server, then stops it"() {
        when:
        Command commandStart = new ApacheCommonsCommand("java -cp target/classes com.cezarykluczynski.carmen" +
                ".lang.stats.util.LanguageDetectorServerSwitcher \"start\"")
        Command commandStop = new ApacheCommonsCommand("java -cp target/classes com.cezarykluczynski.carmen" +
                ".lang.stats.util.LanguageDetectorServerSwitcher \"stop\"")
        Result resultStart = Executor.execute(commandStart)
        Result resultStop = Executor.execute(commandStop)

        then:
        resultStart.successFul
        resultStop.successFul
    }

}

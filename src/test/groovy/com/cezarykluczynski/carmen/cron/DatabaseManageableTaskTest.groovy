package com.cezarykluczynski.carmen.cron

import com.beust.jcommander.internal.Lists
import com.cezarykluczynski.carmen.dao.pub.CronsDAO
import com.cezarykluczynski.carmen.model.pub.Cron
import spock.lang.Specification

class DatabaseManageableTaskTest extends Specification {

    private static final String NAME = "TestableCron"
    private static final String SERVER = "testable-server"

    private DatabaseManageableTask databaseManageableTask

    private CronsDAO cronsDAOMock

    private Cron cron

    void "task can be enabled"() {
        given:
        createTask false, false, false

        when:
        databaseManageableTask.enable()

        then:
        1 * cronsDAOMock.update(cron)
        cron.isEnabled()
    }

    def "task can be disabled"() {
        given:
        createTask true, false, true

        when:
        databaseManageableTask.disable()

        then:
        1 * cronsDAOMock.update(cron)
        !cron.isEnabled()
    }

    def "isOff is false when task is enabled and running"() {
        given:
        createTask true, true, false

        expect:
        !databaseManageableTask.isOff()
    }

    def "isOff is false when task is disabled and running"() {
        given:
        createTask false, true, true

        expect:
        !databaseManageableTask.isOff()
    }

    def "isOff is true when task is disabled and not running"() {
        given:
        createTask false, false, false

        expect:
        databaseManageableTask.isOff()
    }

    def "isEnabled is true when task is enabled"() {
        given:
        createTask true, false, true

        expect:
        databaseManageableTask.isEnabled()
    }

    def "isEnabled is false when task is disabled"() {
        given:
        createTask false, false, false

        expect:
        !databaseManageableTask.isEnabled()
    }

    def "isRunning is true when task is running"() {
        given:
        createTask false, true, true

        expect:
        databaseManageableTask.isRunning()
    }

    def "isRunning is when task is not running"() {
        given:
        createTask false, false, false

        expect:
        !databaseManageableTask.isRunning()
    }

    private void createTask(boolean enabled, boolean running, boolean withServer) {
        cronsDAOMock = Mock CronsDAO

        cron = new Cron(
                name: NAME,
                server: SERVER,
                enabled: enabled,
                running: running
        )

        cronsDAOMock.findByName(NAME) >> Lists.newArrayList(cron)
        cronsDAOMock.findByNameAndServer(NAME, SERVER) >> cron

        if (withServer) {
            databaseManageableTask = new DatabaseManageableTask(cronsDAOMock, NAME, SERVER)
        } else {
            databaseManageableTask = new DatabaseManageableTask(cronsDAOMock, NAME)
        }
    }
}

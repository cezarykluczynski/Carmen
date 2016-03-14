package com.cezarykluczynski.carmen.cron

import com.beust.jcommander.internal.Lists
import com.cezarykluczynski.carmen.dao.pub.CronsDAO
import com.cezarykluczynski.carmen.model.pub.Cron
import org.testng.Assert
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class DatabaseManageableTaskTest {

    private DatabaseManageableTask databaseManageableTask

    private CronsDAO cronsDAOMock

    private Cron cron

    private static final String name = "TestableCron"
    private static final String server = "testable-server"

    @Test
    void enable() {
        // setup
        createTask false, false, false

        // exercise
        databaseManageableTask.enable()

        // assertion
        Assert.assertTrue cron.isEnabled()
        verify(cronsDAOMock).update cron
    }

    @Test
    void disable() {
        // setup
        createTask true, false, true

        // exercise
        databaseManageableTask.disable()

        // assertion
        Assert.assertFalse cron.isEnabled()
        verify(cronsDAOMock).update cron
    }

    @Test
    void "isOff when is enabled and is running"() {
        // setup
        createTask true, true, false

        // exercise, assertion
        Assert.assertFalse databaseManageableTask.isOff()
    }

    @Test
    void "isOff when is disabled and is running"() {
        // setup
        createTask false, true, true

        // exercise, assertion
        Assert.assertFalse databaseManageableTask.isOff()
    }


    @Test
    void "isOff when is disabled and is not running"() {
        // setup
        createTask false, false, false

        // exercise, assertion
        Assert.assertTrue databaseManageableTask.isOff()
    }

    @Test
    void "isEnabled when it is enabled"() {
        // setup
        createTask true, false, true

        // exercise, assertion
        Assert.assertTrue databaseManageableTask.isEnabled()
    }

    @Test
    void "isEnabled when it is disabled"() {
        // setup
        createTask false, false, false

        // exercise, assertion
        Assert.assertFalse databaseManageableTask.isEnabled()
    }


    @Test
    void "isRunning when it is running"() {
        // setup
        createTask false, true, true

        // exercise, assertion
        Assert.assertTrue databaseManageableTask.isRunning()
    }

    @Test
    void "isRunning when it is not running"() {
        // setup
        createTask false, false, false

        // exercise, assertion
        Assert.assertFalse databaseManageableTask.isRunning()
    }

    private void createTask(boolean enabled, boolean running, boolean withServer) {
        cronsDAOMock = mock CronsDAO.class

        cron = new Cron()
        cron.setName name
        cron.setServer server
        cron.setEnabled enabled
        cron.setRunning running

        when cronsDAOMock.findByName(name) thenReturn Lists.newArrayList(cron)
        when cronsDAOMock.findByNameAndServer(name, server) thenReturn cron

        if (withServer) {
            databaseManageableTask = new DatabaseManageableTask(cronsDAOMock, name, server)
        } else {
            databaseManageableTask = new DatabaseManageableTask(cronsDAOMock, name)
        }
    }
}

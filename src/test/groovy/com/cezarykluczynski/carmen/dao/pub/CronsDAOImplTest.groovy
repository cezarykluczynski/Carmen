package com.cezarykluczynski.carmen.dao.pub

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.model.pub.Cron
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class CronsDAOImplTest extends AbstractTestNGSpringContextTests {

    private CronsDAOImpl cronsDAOImpl

    @Autowired
    private CronsDAOImplFixtures cronsDAOImplFixtures

    @Autowired
    private SessionFactory sessionFactory

    private String name
    private String server

    @BeforeClass
    void setUpClass() {
        cronsDAOImpl = new CronsDAOImpl(sessionFactory)
    }

    @BeforeMethod
    void setUp() {
        name = "name_" + System.currentTimeMillis()
        server = "server_" + System.currentTimeMillis()
    }

    @Test
    void findByName() {
        // setup
        Cron cron = cronsDAOImplFixtures.createEntityWithNameAndServer name, server

        // exercise, assertion
        Assert.assertEquals cronsDAOImpl.findByName(name).get(0).getId(), cron.getId()

        // teardown
        cronsDAOImplFixtures.deleteEntity cron
    }

    @Test
    void findByNameAndServer() {
        // setup
        Cron cron = cronsDAOImplFixtures.createEntityWithNameAndServer name, server

        // exercise, assertion
        Assert.assertEquals cronsDAOImpl.findByNameAndServer(name, server).getId(), cron.getId()

        // teardown
        cronsDAOImplFixtures.deleteEntity cron
    }

    @Test
    void create() {
        // setup
        Cron cron = createCronEntity()

        // exercise
        cronsDAOImpl.create cron

        // assertion
        Assert.assertNotNull cronsDAOImpl.findByNameAndServer(name, server).getId()

        // teardown
        cronsDAOImplFixtures.deleteEntity cron
    }

    @Test
    void update() {
        // setup
        Cron cron = createCronEntity()
        cronsDAOImpl.create cron
        String serverRenamed = server + "2"
        cron.setServer serverRenamed

        // exercise
        cronsDAOImpl.update cron

        // assertion
        Assert.assertNull cronsDAOImpl.findByNameAndServer(name, server)
        Assert.assertNotNull cronsDAOImpl.findByNameAndServer(name, serverRenamed)

        // teardown
        cronsDAOImplFixtures.deleteEntity cron
    }

    @Test
    void delete() {
        // setup
        Cron cron = cronsDAOImplFixtures.createEntityWithNameAndServer name, server

        // exercise
        cronsDAOImpl.delete cron

        // assertion
        Assert.assertNull cronsDAOImpl.findByNameAndServer(name, server)
    }

    private Cron createCronEntity() {
        Cron cron = new Cron()
        cron.setName name
        cron.setServer server
        return cron
    }

}

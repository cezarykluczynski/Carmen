package com.cezarykluczynski.carmen.dao.pub

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.model.pub.Cron
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired

class CronsDAOImplTest extends IntegrationTest {

    private CronsDAOImpl cronsDAOImpl

    @Autowired
    private CronsDAOImplFixtures cronsDAOImplFixtures

    @Autowired
    private SessionFactory sessionFactory

    private String name
    private String server

    def setup() {
        cronsDAOImpl = new CronsDAOImpl(sessionFactory)
        name = "name_" + System.currentTimeMillis()
        server = "server_" + System.currentTimeMillis()
    }

    def "entity is found by name"() {
        given:
        Cron cron = cronsDAOImplFixtures.createEntityWithNameAndServer name, server

        when:
        Cron foundCron = cronsDAOImpl.findByName(name).get(0)

        then:
        foundCron.id == cron.id

        cleanup:
        cronsDAOImplFixtures.deleteEntity cron
    }

    def "finds all entities"() {
        given:
        Cron cron = cronsDAOImplFixtures.createEntityWithNameAndServer name

        when:
        Optional<Cron> foundCron = cronsDAOImpl.findAll().stream()
                .filter{cronListElement -> cronListElement.name == cron.name}
                .findFirst()

        then:
        foundCron.isPresent()

        cleanup:
        cronsDAOImplFixtures.deleteEntity cron
    }

    def "entity is found by name and server"() {
        given:
        Cron cron = cronsDAOImplFixtures.createEntityWithNameAndServer name, server

        when:
        Cron foundCron =  cronsDAOImpl.findByNameAndServer(name, server)

        then:
        foundCron.id == cron.id

        cleanup:
        cronsDAOImplFixtures.deleteEntity cron
    }

    def "entity is created"() {
        given:
        Cron cron = createCronEntity()

        when:
        cronsDAOImpl.create cron

        then:
        cronsDAOImpl.findByNameAndServer(name, server).id != null

        cleanup:
        cronsDAOImplFixtures.deleteEntity cron
    }

    def "entity is updated"() {
        given:
        Cron cron = createCronEntity()
        cronsDAOImpl.create cron
        String serverRenamed = server + "2"
        cron.setServer serverRenamed

        when:
        cronsDAOImpl.update cron

        then:
        cronsDAOImpl.findByNameAndServer(name, server) == null
        cronsDAOImpl.findByNameAndServer(name, serverRenamed) != null

        cleanup:
        cronsDAOImplFixtures.deleteEntity cron
    }

    def "entity is deleted"() {
        given:
        Cron cron = cronsDAOImplFixtures.createEntityWithNameAndServer name, server

        when:
        cronsDAOImpl.delete cron

        then:
        cronsDAOImpl.findByNameAndServer(name, server) == null
    }

    private Cron createCronEntity() {
        Cron cron = new Cron()
        cron.setName name
        cron.setServer server
        return cron
    }

}

package com.cezarykluczynski.carmen.cron.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.cron.model.entity.Cron
import org.springframework.beans.factory.annotation.Autowired

class CronRepositoryTest extends IntegrationTest {

    @Autowired
    private CronRepositoryFixtures cronRepositoryFixtures

    @Autowired
    private CronRepository cronRepository

    private String name
    private String server

    def setup() {
        name = "name_" + System.currentTimeMillis()
        server = "server_" + System.currentTimeMillis()
    }

    def "entity is found by name"() {
        given:
        Cron cron = cronRepositoryFixtures.createEntityWithNameAndServer name, server

        when:
        Cron foundCron = cronRepository.findAllByName(name).get(0)

        then:
        foundCron.id == cron.id

        cleanup:
        cronRepositoryFixtures.deleteEntity cron
    }

    def "finds all entities"() {
        given:
        Cron cron = cronRepositoryFixtures.createEntityWithNameAndServer name

        when:
        Optional<Cron> foundCron = cronRepository.findAll().stream()
                .filter{cronListElement -> cronListElement.name == cron.name}
                .findFirst()

        then:
        foundCron.isPresent()

        cleanup:
        cronRepositoryFixtures.deleteEntity cron
    }

    def "entity is found by name and server"() {
        given:
        Cron cron = cronRepositoryFixtures.createEntityWithNameAndServer name, server

        when:
        Cron foundCron =  cronRepository.findFirstByNameAndServer(name, server)

        then:
        foundCron.id == cron.id

        cleanup:
        cronRepositoryFixtures.deleteEntity cron
    }

    def "entity is created"() {
        given:
        Cron cron = createCronEntity()

        when:
        cronRepository.save cron

        then:
        cronRepository.findFirstByNameAndServer(name, server).id != null

        cleanup:
        cronRepositoryFixtures.deleteEntity cron
    }

    def "entity is updated"() {
        given:
        Cron cron = createCronEntity()
        cronRepository.save cron
        String serverRenamed = server + "2"
        cron.setServer serverRenamed

        when:
        cronRepository.save cron

        then:
        cronRepository.findFirstByNameAndServer(name, server) == null
        cronRepository.findFirstByNameAndServer(name, serverRenamed) != null

        cleanup:
        cronRepositoryFixtures.deleteEntity cron
    }

    def "entity is deleted"() {
        given:
        Cron cron = cronRepositoryFixtures.createEntityWithNameAndServer name, server

        when:
        cronRepository.delete cron

        then:
        cronRepository.findFirstByNameAndServer(name, server) == null
    }

    private Cron createCronEntity() {
        Cron cron = new Cron()
        cron.setName name
        cron.setServer server
        return cron
    }

}

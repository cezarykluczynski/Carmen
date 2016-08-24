package com.cezarykluczynski.carmen

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.google.common.collect.Lists
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [TestableApplicationConfiguration.class])
@WebAppConfiguration
@org.springframework.boot.test.IntegrationTest
public abstract class IntegrationTest extends Specification {

    def SessionFactory createSessionFactoryMockWithEmptyCriteriaList(Class clazz) {
        return createSessionFactoryMock(clazz, false)
    }

    def SessionFactory createSessionFactoryMockWithEmptyCriteriaListAndMethods(Class clazz) {
        return createSessionFactoryMock(clazz, true)
    }

    private SessionFactory createSessionFactoryMock(Class clazz, Boolean withMethods) {
        SessionFactory sessionFactoryMock = Mock SessionFactory
        Session sessionMock = Mock Session

        Criteria criteriaMock = Mock Criteria
        if (withMethods) {
            criteriaMock.add(*_) >> criteriaMock
            criteriaMock.addOrder(*_) >> criteriaMock
            criteriaMock.setMaxResults(1) >> criteriaMock
        }
        criteriaMock.list() >> Lists.newArrayList()

        sessionMock.createCriteria(clazz) >> criteriaMock
        sessionFactoryMock.openSession() >> sessionMock

        return sessionFactoryMock
    }

}

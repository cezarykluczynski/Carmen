package com.cezarykluczynski.carmen.fixture.org.hibernate

import org.springframework.stereotype.Component

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Criteria

import static org.mockito.Mockito.when
import static org.mockito.Mockito.mock

import java.lang.Class

@Component
class SessionFactoryFixtures {

    SessionFactory createSessionFactoryMockWithEmptyCriteriaList(Class clazz) {
        SessionFactory sessionFactoryMock = mock SessionFactory.class
        Session sessionMock = mock Session.class
        Criteria criteriaMock = mock Criteria.class
        when sessionFactoryMock.openSession() thenReturn sessionMock
        when sessionMock.createCriteria(clazz) thenReturn criteriaMock
        when criteriaMock.list() thenReturn new ArrayList<?>()
        return sessionFactoryMock
    }

}

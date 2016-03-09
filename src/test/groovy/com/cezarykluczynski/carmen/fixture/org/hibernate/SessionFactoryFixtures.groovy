package com.cezarykluczynski.carmen.fixture.org.hibernate

import org.springframework.stereotype.Component

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Criteria

import static org.mockito.Mockito.any
import static org.mockito.Mockito.when
import static org.mockito.Mockito.mock

class SessionFactoryFixtures {

    static SessionFactory createSessionFactoryMockWithEmptyCriteriaList(Class clazz) {
        return createSessionFactoryMock(clazz, false)
    }

    static SessionFactory createSessionFactoryMockWithEmptyCriteriaListAndMethods(Class clazz) {
        return createSessionFactoryMock(clazz, true)
    }

    private static SessionFactory createSessionFactoryMock(Class clazz, Boolean withMethods) {
        SessionFactory sessionFactoryMock = mock SessionFactory.class
        Session sessionMock = mock Session.class

        Criteria criteriaMock = mock Criteria.class
        if (withMethods) {
            when criteriaMock.add(any()) thenReturn criteriaMock
            when criteriaMock.addOrder(any()) thenReturn criteriaMock
            when criteriaMock.setMaxResults(1) thenReturn criteriaMock
        }
        when criteriaMock.list() thenReturn new ArrayList<?>()

        when sessionMock.createCriteria(clazz) thenReturn criteriaMock
        when sessionFactoryMock.openSession() thenReturn sessionMock

        return sessionFactoryMock
    }

}

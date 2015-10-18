package com.cezarykluczynski.carmen.aspect.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserRepositoriesPropagation

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.doNothing
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserPropagationRepositoriesTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImpl githubUserDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Mock
    UserRepositoriesPropagation userRepositoriesPropagation

    @Autowired
    @InjectMocks
    UserPropagationRepositories userPropagationRepositories

    User userEntity

    @BeforeMethod
    void setUp() {
        userRepositoriesPropagation = mock UserRepositoriesPropagation.class
        doNothing().when(userRepositoriesPropagation).propagate()
        MockitoAnnotations.initMocks this
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
    }

    @Test
    void repositoriesPropagationIsFired() {
        // exercise
        githubUserDAOImpl.createOrUpdateRequestedEntity userEntity.getLogin()

        // assertion
        verify(userRepositoriesPropagation).propagate()
    }

    @AfterMethod
    void tearDown() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}

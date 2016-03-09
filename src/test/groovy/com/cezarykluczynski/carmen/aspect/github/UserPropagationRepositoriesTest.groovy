package com.cezarykluczynski.carmen.aspect.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserRepositoriesPropagation

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.doNothing
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

@ContextConfiguration(
        classes = TestableApplicationConfiguration.class,
        loader = SpringApplicationContextLoader.class,
        locations = ["classpath:applicationContext.xml"]
)
@WebAppConfiguration
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

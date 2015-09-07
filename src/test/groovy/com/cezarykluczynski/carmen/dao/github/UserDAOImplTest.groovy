package com.cezarykluczynski.carmen.dao.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet

import org.testng.annotations.Test

import org.testng.Assert

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserDAOImpl githubUserDAOImpl

    @Test
    void create() {
        // setup
        Long id = 2147483647
        String login = "random_login" + System.currentTimeMillis()
        String name = "Random Name"
        String avatarUrl = "http://avatar.url/"
        String type = "User"
        boolean siteAdmin = false
        String company = "Company"
        String blog = "http://blog.url/"
        String location = "Place"
        String email = login + "@example.com"
        boolean hireable = false

        UserSet userSet = new UserSet(
            id,
            login,
            name,
            avatarUrl,
            type,
            siteAdmin,
            company,
            blog,
            location,
            email,
            hireable
        )

        User userEntity = githubUserDAOImpl.create userSet

        Assert.assertNotNull userEntity.getId()
        Assert.assertEquals userEntity.getLogin(), login
        Assert.assertEquals userEntity.getName(), name
        Assert.assertEquals userEntity.getAvatarUrl(), avatarUrl
        Assert.assertEquals userEntity.getSiteAdmin(), siteAdmin
        Assert.assertEquals userEntity.getCompany(), company
        Assert.assertEquals userEntity.getBlog(), blog
        Assert.assertEquals userEntity.getLocation(), location
        Assert.assertEquals userEntity.getEmail(), email
        Assert.assertEquals userEntity.getHireable(), hireable

        // teardown
        githubUserDAOImpl.delete userEntity
    }

    @Test
    void updateUserEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()
        String newLogin = "new_random_login" + System.currentTimeMillis()

        userEntity.setLogin newLogin
        githubUserDAOImpl.update userEntity
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertEquals userEntity.getId(), userEntityUpdated.getId()
        Assert.assertEquals newLogin, userEntityUpdated.getLogin()

        // teardown
        githubUserDAOImpl.delete userEntity
    }

    @Test
    void updateUserEntityUsingUserSet() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()
        String newLogin = "new_random_login" + System.currentTimeMillis()
        UserSet userSet = new UserSet(null, newLogin)

        githubUserDAOImpl.update userEntity, userSet
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin
        Assert.assertEquals userEntity.getId(), userEntityUpdated.getId()
        Assert.assertEquals newLogin, userEntityUpdated.getLogin()

        // teardown
        githubUserDAOImpl.delete userEntity
    }

    @Test
    void linkFollowerWithFolloweeWithNoPreviousLinkExisting() {
        // setup
        User userEntityFollowee = githubUserDAOImplFixtures.createNotFoundEntity()
        User userEntityFollower = githubUserDAOImplFixtures.createNotFoundEntity()

        githubUserDAOImpl.linkFollowerWithFollowee userEntityFollower, userEntityFollowee
        Assert.assertEquals githubUserDAOImpl.countFollowees(userEntityFollower), 1
        Assert.assertEquals githubUserDAOImpl.countFollowers(userEntityFollowee), 1

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower
    }

    @Test
    void linkFollowerWithFolloweeWithPreviousLinkExisting() {
        // setup
        User userEntityFollowee = githubUserDAOImplFixtures.createNotFoundEntity()
        User userEntityFollower = githubUserDAOImplFixtures.createNotFoundEntity()

        githubUserDAOImpl.linkFollowerWithFollowee userEntityFollower, userEntityFollowee
        githubUserDAOImpl.linkFollowerWithFollowee userEntityFollower, userEntityFollowee
        Assert.assertEquals githubUserDAOImpl.countFollowees(userEntityFollower), 1
        Assert.assertEquals githubUserDAOImpl.countFollowers(userEntityFollowee), 1

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower
    }

}

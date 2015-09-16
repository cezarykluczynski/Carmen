package com.cezarykluczynski.carmen.dao.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
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
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

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

        // exercise
        User userEntity = githubUserDAOImpl.create userSet

        // assertion
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
    void findByLogin() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()

        // exercise
        User userEntityFound = githubUserDAOImpl.findByLogin userEntity.getLogin()

        // assertion
        Assert.assertEquals userEntityFound.getId(), userEntity.getId()

        // teardown
        githubUserDAOImpl.delete userEntity
    }

    @Test
    void findByIdInteger() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()

        User userEntityFound = githubUserDAOImpl.findById userEntity.getId().intValue()
        Assert.assertEquals userEntityFound.getId(), userEntity.getId()

        // teardown
        githubUserDAOImpl.delete userEntity
    }

    @Test
    void findByIdLong() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()

        User userEntityFound = githubUserDAOImpl.findById userEntity.getId()
        Assert.assertEquals userEntityFound.getId(), userEntity.getId()

        // teardown
        githubUserDAOImpl.delete userEntity
    }

    @Test
    void findByNotExistingId() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()

        User userEntityFound = githubUserDAOImpl.findById 2147483647
        Assert.assertNull userEntityFound

        // teardown
        githubUserDAOImpl.delete userEntity
    }

    @Test
    void updateUserEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()
        String newLogin = "new_random_login${System.currentTimeMillis()}"
        userEntity.setLogin newLogin

        // exercise
        githubUserDAOImpl.update userEntity

        // assertion
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
        String newLogin = "new_random_login${System.currentTimeMillis()}"
        UserSet userSet = new UserSet(null, newLogin)

        // exercise
        githubUserDAOImpl.update userEntity, userSet

        // assertion
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

        // exercise
        githubUserDAOImpl.linkFollowerWithFollowee userEntityFollower, userEntityFollowee

        // assertion
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

        // exercise
        githubUserDAOImpl.linkFollowerWithFollowee userEntityFollower, userEntityFollowee
        githubUserDAOImpl.linkFollowerWithFollowee userEntityFollower, userEntityFollowee

        // assertion
        Assert.assertEquals githubUserDAOImpl.countFollowees(userEntityFollower), 1
        Assert.assertEquals githubUserDAOImpl.countFollowers(userEntityFollowee), 1

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower
    }

    @Test
    void findUserInReportFollowersFolloweesPhaseExistingEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "report")
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "report")

        // exercise
        User userEntityFound = githubUserDAOImpl.findUserInReportFollowersFolloweesPhase()

        // assertion
        Assert.assertEquals userEntity.getId(), userEntityFound.getId()

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
    }

    @Test
    void findUserInReportFollowersFolloweesPhaseNonExistingEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "report")

        // exercise
        User userEntityFound = githubUserDAOImpl.findUserInReportFollowersFolloweesPhase()

        // assertion
        Assert.assertNull userEntityFound

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
    }

    @Test
    void countFollowers() {
        // setup
        User userEntityFollowee = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        User userEntityFollower1 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        User userEntityFollower2 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        Session session = sessionFactory.openSession()
        session.createSQLQuery('''\
                INSERT INTO github.user_followers (follower_id, followee_id) VALUES
                (:userEntityFollower1Id, :userEntityFolloweeId),
                (:userEntityFollower2Id, :userEntityFolloweeId)
            ''')
            .setParameter("userEntityFolloweeId", userEntityFollowee.getId())
            .setParameter("userEntityFollower1Id", userEntityFollower1.getId())
            .setParameter("userEntityFollower2Id", userEntityFollower2.getId())
            .executeUpdate()
        session.close()

        Assert.assertEquals githubUserDAOImpl.countFollowers(userEntityFollowee), 2

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower1
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower2
    }

    @Test
    void countFollowees() {
        // setup
        User userEntityFollower = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        User userEntityFollowee1 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        User userEntityFollowee2 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        Session session = sessionFactory.openSession()
        session.createSQLQuery('''\
                INSERT INTO github.user_followers (follower_id, followee_id) VALUES
                (:userEntityFollowerId, :userEntityFollowee1Id),
                (:userEntityFollowerId, :userEntityFollowee2Id)
            ''')
            .setParameter("userEntityFollowerId", userEntityFollower.getId())
            .setParameter("userEntityFollowee1Id", userEntityFollowee1.getId())
            .setParameter("userEntityFollowee2Id", userEntityFollowee2.getId())
            .executeUpdate()
        session.close()

        // exercise, assertion
        Assert.assertEquals githubUserDAOImpl.countFollowees(userEntityFollower), 2

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee1
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee2
    }

    @Test
    void countFollowersFollowing() {
        // setup
        User userEntityBase = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        User userEntitySatelite1 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        User userEntitySatelite2 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        User userEntitySatelite3 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        User userEntitySatelite4 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        Session session = sessionFactory.openSession()
        session.createSQLQuery('''\
                INSERT INTO github.user_followers (follower_id, followee_id) VALUES
                (:userEntityBaseId, :userEntitySatelite1Id),
                (:userEntityBaseId, :userEntitySatelite2Id),
                (:userEntityBaseId, :userEntitySatelite3Id),
                (:userEntitySatelite2Id, :userEntityBaseId),
                (:userEntitySatelite3Id, :userEntityBaseId),
                (:userEntitySatelite4Id, :userEntityBaseId)
            ''')
            .setParameter("userEntityBaseId", userEntityBase.getId())
            .setParameter("userEntitySatelite1Id", userEntitySatelite1.getId())
            .setParameter("userEntitySatelite2Id", userEntitySatelite2.getId())
            .setParameter("userEntitySatelite3Id", userEntitySatelite3.getId())
            .setParameter("userEntitySatelite4Id", userEntitySatelite4.getId())
            .executeUpdate()
        session.close()

        // exercise, assertion
        Assert.assertEquals githubUserDAOImpl.countFollowersFollowing(userEntityBase), 2

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntityBase
        githubUserDAOImplFixtures.deleteUserEntity userEntitySatelite1
        githubUserDAOImplFixtures.deleteUserEntity userEntitySatelite2
        githubUserDAOImplFixtures.deleteUserEntity userEntitySatelite3
        githubUserDAOImplFixtures.deleteUserEntity userEntitySatelite4
    }

}

package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired

class UserDAOImplTest extends IntegrationTest {

    private static final Long ID = 2147483647
    private static final String LOGIN = UserDAOImplFixtures.generateRandomLogin()
    private static final String NAME = "Random Name"
    private static final String AVATAR_URL = "http://avatar.url/"
    private static final String TYPE = "User"
    private static final boolean SITE_ADMIN = false
    private static final String COMPANY = "Company"
    private static final String BLOG = "http://blog.url/"
    private static final String LOCATION = "Place"
    private static final String EMAIL = LOGIN + "@example.com"
    private static final boolean HIREABLE = false

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    private UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    private UserDAO githubUserDAOImpl

    def "should be created"() {
        given:
        UserSet userSet = UserSet.builder()
            .id(ID)
            .login(LOGIN)
            .name(NAME)
            .avatarUrl(AVATAR_URL)
            .type(TYPE)
            .siteAdmin(SITE_ADMIN)
            .company(COMPANY)
            .blog(BLOG)
            .location(LOCATION)
            .email(EMAIL)
            .hireable(HIREABLE)
            .build()

        when:
        User userEntity = githubUserDAOImpl.create userSet

        then:
        userEntity.getId() != null
        userEntity.getLogin() == LOGIN
        userEntity.getName() == NAME
        userEntity.getAvatarUrl() == AVATAR_URL
        userEntity.isSiteAdmin() == SITE_ADMIN
        userEntity.getCompany() == COMPANY
        userEntity.getBlog() == BLOG
        userEntity.getLocation() == LOCATION
        userEntity.getEmail() == EMAIL
        userEntity.isHireable() == HIREABLE

        cleanup:
        githubUserDAOImpl.delete userEntity
    }

    def "entity is found by login"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()

        when:
        User userEntityFound = githubUserDAOImpl.findByLogin userEntity.getLogin()

        then:
        userEntityFound.id == userEntity.id

        cleanup:
        githubUserDAOImpl.delete userEntity
    }

    def "entity is found by integer id"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()

        when:
        User userEntityFound = githubUserDAOImpl.findById userEntity.getId().intValue()

        then:
        userEntityFound.id == userEntity.id

        cleanup:
        githubUserDAOImpl.delete userEntity
    }

    def "entity is found by long id"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()

        when:
        User userEntityFound = githubUserDAOImpl.findById userEntity.getId()

        then:
        userEntityFound.id == userEntity.id

        cleanup:
        githubUserDAOImpl.delete userEntity
    }

    def "non-existing entity is not found"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()

        when:
        User userEntityFound = githubUserDAOImpl.findById 2147483647

        then:
        userEntityFound == null

        cleanup:
        githubUserDAOImpl.delete userEntity
    }

    def "entity is updated"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        userEntity.setLogin newLogin

        when:
        githubUserDAOImpl.update userEntity
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin

        then:
        userEntity.id == userEntityUpdated.id
        newLogin == userEntityUpdated.login

        cleanup:
        githubUserDAOImpl.delete userEntity
    }

    def "entity is updated using UserSet"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createNotFoundEntity()
        String newLogin = githubUserDAOImplFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()

        when:
        githubUserDAOImpl.update userEntity, userSet
        User userEntityUpdated = githubUserDAOImpl.findByLogin newLogin

        then:
        userEntity.id == userEntityUpdated.id
        newLogin == userEntityUpdated.login

        cleanup:
        githubUserDAOImpl.delete userEntity
    }

    def "links follower with followee with no previous link existing"() {
        given:
        User userEntityFollowee = githubUserDAOImplFixtures.createNotFoundEntity()
        User userEntityFollower = githubUserDAOImplFixtures.createNotFoundEntity()

        when:
        githubUserDAOImpl.linkFollowerWithFollowee userEntityFollower, userEntityFollowee

        then:
        githubUserDAOImpl.countFollowees(userEntityFollower) == 1
        githubUserDAOImpl.countFollowers(userEntityFollowee) == 1

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower
    }

    def "links follower with followee with previous link existing"() {
        given:
        User userEntityFollowee = githubUserDAOImplFixtures.createNotFoundEntity()
        User userEntityFollower = githubUserDAOImplFixtures.createNotFoundEntity()

        when:
        githubUserDAOImpl.linkFollowerWithFollowee userEntityFollower, userEntityFollowee
        githubUserDAOImpl.linkFollowerWithFollowee userEntityFollower, userEntityFollowee

        then:
        githubUserDAOImpl.countFollowees(userEntityFollower) == 1
        githubUserDAOImpl.countFollowers(userEntityFollowee) == 1

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower
    }

    def "finds existing user in report phase"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        propagationsUserFollowersDAOImplFixtures.createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "report")
        propagationsUserFollowingDAOImplFixtures.createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "report")

        when:
        User userEntityFound = githubUserDAOImpl.findUserInReportFollowersFolloweesPhase()

        then:
        userEntity.id == userEntityFound.id

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "does not find non-existing user in report phase"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        propagationsUserFollowersDAOImplFixtures.createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
        propagationsUserFollowingDAOImplFixtures.createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "report")

        when:
        User userEntityFound = githubUserDAOImpl.findUserInReportFollowersFolloweesPhase()

        then:
        userEntityFound == null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "count followers"() {
        given:
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

        when:
        Integer followersCount = githubUserDAOImpl.countFollowers(userEntityFollowee)

        then:
        followersCount == 2

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower1
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower2
    }

    def "counts followees"() {
        given:
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

        when:
        Integer followeesCount = githubUserDAOImpl.countFollowees(userEntityFollower)

        then:
        followeesCount == 2

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollower
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee1
        githubUserDAOImplFixtures.deleteUserEntity userEntityFollowee2
    }

    def "count followers following"() {
        given:
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

        when:
        Integer followersFollowingCount = githubUserDAOImpl.countFollowersFollowing(userEntityBase)

        then:
        followersFollowingCount == 2

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntityBase
        githubUserDAOImplFixtures.deleteUserEntity userEntitySatelite1
        githubUserDAOImplFixtures.deleteUserEntity userEntitySatelite2
        githubUserDAOImplFixtures.deleteUserEntity userEntitySatelite3
        githubUserDAOImplFixtures.deleteUserEntity userEntitySatelite4
    }

    def "finds highest GitHub user id"() {
        given:
        Integer gitHubUserId = System.currentTimeMillis()
        User userEntity = githubUserDAOImplFixtures.createEntityWithGitHubUserId gitHubUserId

        when:
        Integer highestGitHubUserId = githubUserDAOImpl.findHighestGitHubUserId()

        then:
        highestGitHubUserId == gitHubUserId

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}

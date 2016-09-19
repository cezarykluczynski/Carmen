package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.set.github.UserDTO as UserSet
import com.cezarykluczynski.carmen.util.db.TransactionalExecutor
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryTest extends IntegrationTest {

    private static final Long ID = 2147483647
    private static final String LOGIN = UserRepositoryFixtures.generateRandomLogin()
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
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserFollowersRepositoryFixtures userFollowersRepositoryFixtures

    @Autowired
    private UserFollowingRepositoryFixtures userFollowingRepositoryFixtures

    @Autowired
    private UserRepository userRepository

    @Autowired
    TransactionalExecutor transactionalExecutor

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
        User userEntity = userRepository.create userSet

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
        userRepository.delete userEntity
    }

    def "entity is found by login"() {
        given:
        User userEntity = userRepositoryFixtures.createNotFoundEntity()

        when:
        User userEntityFound = userRepository.findByLogin userEntity.getLogin()

        then:
        userEntityFound.id == userEntity.id

        cleanup:
        userRepository.delete userEntity
    }

    def "entity is found by integer id"() {
        given:
        User userEntity = userRepositoryFixtures.createNotFoundEntity()

        when:
        User userEntityFound = userRepository.findById userEntity.getId().intValue()

        then:
        userEntityFound.id == userEntity.id

        cleanup:
        userRepository.delete userEntity
    }

    def "entity is found by long id"() {
        given:
        User userEntity = userRepositoryFixtures.createNotFoundEntity()

        when:
        User userEntityFound = userRepository.findById userEntity.getId()

        then:
        userEntityFound.id == userEntity.id

        cleanup:
        userRepository.delete userEntity
    }

    def "non-existing entity is not found"() {
        given:
        User userEntity = userRepositoryFixtures.createNotFoundEntity()

        when:
        User userEntityFound = userRepository.findById 2147483647

        then:
        userEntityFound == null

        cleanup:
        userRepository.delete userEntity
    }

    def "entity is updated"() {
        given:
        User userEntity = userRepositoryFixtures.createNotFoundEntity()
        String newLogin = userRepositoryFixtures.generateRandomLogin()
        userEntity.setLogin newLogin

        when:
        userRepository.save userEntity
        User userEntityUpdated = userRepository.findByLogin newLogin

        then:
        userEntity.id == userEntityUpdated.id
        newLogin == userEntityUpdated.login

        cleanup:
        userRepository.delete userEntity
    }

    def "entity is updated using UserSet"() {
        given:
        User userEntity = userRepositoryFixtures.createNotFoundEntity()
        String newLogin = userRepositoryFixtures.generateRandomLogin()
        UserSet userSet = UserSet.builder().login(newLogin).build()

        when:
        userRepository.update userEntity, userSet
        User userEntityUpdated = userRepository.findByLogin newLogin

        then:
        userEntity.id == userEntityUpdated.id
        newLogin == userEntityUpdated.login

        cleanup:
        userRepository.delete userEntity
    }

    def "links follower with followee with no previous link existing"() {
        given:
        User userEntityFollowee = userRepositoryFixtures.createNotFoundEntity()
        User userEntityFollower = userRepositoryFixtures.createNotFoundEntity()

        when:
        userRepository.linkFollowerWithFollowee userEntityFollower, userEntityFollowee

        then:
        userRepository.countFollowees(userEntityFollower) == 1
        userRepository.countFollowers(userEntityFollowee) == 1

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntityFollowee
        userRepositoryFixtures.deleteUserEntity userEntityFollower
    }

    def "links follower with followee with previous link existing"() {
        given:
        User userEntityFollowee = userRepositoryFixtures.createNotFoundEntity()
        User userEntityFollower = userRepositoryFixtures.createNotFoundEntity()

        when:
        userRepository.linkFollowerWithFollowee userEntityFollower, userEntityFollowee
        userRepository.linkFollowerWithFollowee userEntityFollower, userEntityFollowee

        then:
        userRepository.countFollowees(userEntityFollower) == 1
        userRepository.countFollowers(userEntityFollowee) == 1

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntityFollowee
        userRepositoryFixtures.deleteUserEntity userEntityFollower
    }

    def "finds existing user in report phase"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userFollowersRepositoryFixtures.createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "report")
        userFollowingRepositoryFixtures.createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "report")

        when:
        User userEntityFound = userRepository.findUserInReportFollowersFolloweesPhase()

        then:
        userEntity.id == userEntityFound.id

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "does not find non-existing user in report phase"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userFollowersRepositoryFixtures.createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowingRepositoryFixtures.createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "report")

        when:
        User userEntityFound = userRepository.findUserInReportFollowersFolloweesPhase()

        then:
        userEntityFound == null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "count followers"() {
        given:
        User userEntityFollowee = userRepositoryFixtures.createFoundRequestedUserEntity()
        User userEntityFollower1 = userRepositoryFixtures.createFoundRequestedUserEntity()
        User userEntityFollower2 = userRepositoryFixtures.createFoundRequestedUserEntity()

        transactionalExecutor.execute({ entityManager ->
            entityManager.createNativeQuery('''\
                INSERT INTO github.user_followers (follower_id, followee_id) VALUES
                (:userEntityFollower1Id, :userEntityFolloweeId),
                (:userEntityFollower2Id, :userEntityFolloweeId)
            ''')
                    .setParameter("userEntityFolloweeId", userEntityFollowee.getId())
                    .setParameter("userEntityFollower1Id", userEntityFollower1.getId())
                    .setParameter("userEntityFollower2Id", userEntityFollower2.getId())
                    .executeUpdate()
        })

        when:
        Integer followersCount = userRepository.countFollowers(userEntityFollowee)

        then:
        followersCount == 2

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntityFollowee
        userRepositoryFixtures.deleteUserEntity userEntityFollower1
        userRepositoryFixtures.deleteUserEntity userEntityFollower2
    }

    def "counts followees"() {
        given:
        User userEntityFollower = userRepositoryFixtures.createFoundRequestedUserEntity()
        User userEntityFollowee1 = userRepositoryFixtures.createFoundRequestedUserEntity()
        User userEntityFollowee2 = userRepositoryFixtures.createFoundRequestedUserEntity()

        transactionalExecutor.execute({ entityManager ->
            entityManager.createNativeQuery('''\
                INSERT INTO github.user_followers (follower_id, followee_id) VALUES
                (:userEntityFollowerId, :userEntityFollowee1Id),
                (:userEntityFollowerId, :userEntityFollowee2Id)
            ''')
                    .setParameter("userEntityFollowerId", userEntityFollower.getId())
                    .setParameter("userEntityFollowee1Id", userEntityFollowee1.getId())
                    .setParameter("userEntityFollowee2Id", userEntityFollowee2.getId())
                    .executeUpdate()
        })

        when:
        Integer followeesCount = userRepository.countFollowees(userEntityFollower)

        then:
        followeesCount == 2

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntityFollower
        userRepositoryFixtures.deleteUserEntity userEntityFollowee1
        userRepositoryFixtures.deleteUserEntity userEntityFollowee2
    }

    def "count followers following"() {
        given:
        User userEntityBase = userRepositoryFixtures.createFoundRequestedUserEntity()
        User userEntitySatelite1 = userRepositoryFixtures.createFoundRequestedUserEntity()
        User userEntitySatelite2 = userRepositoryFixtures.createFoundRequestedUserEntity()
        User userEntitySatelite3 = userRepositoryFixtures.createFoundRequestedUserEntity()
        User userEntitySatelite4 = userRepositoryFixtures.createFoundRequestedUserEntity()

        transactionalExecutor.execute({ entityManager ->
            entityManager.createNativeQuery('''\
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
        })


        when:
        Integer followersFollowingCount = userRepository.countFollowersFollowing(userEntityBase)

        then:
        followersFollowingCount == 2

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntityBase
        userRepositoryFixtures.deleteUserEntity userEntitySatelite1
        userRepositoryFixtures.deleteUserEntity userEntitySatelite2
        userRepositoryFixtures.deleteUserEntity userEntitySatelite3
        userRepositoryFixtures.deleteUserEntity userEntitySatelite4
    }

    def "finds highest GitHub user id"() {
        given:
        Integer gitHubUserId = System.currentTimeMillis()
        User userEntity = userRepositoryFixtures.createEntityWithGitHubUserId gitHubUserId

        when:
        Integer highestGitHubUserId = userRepository.findHighestGitHubUserId()

        then:
        highestGitHubUserId == gitHubUserId

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

}

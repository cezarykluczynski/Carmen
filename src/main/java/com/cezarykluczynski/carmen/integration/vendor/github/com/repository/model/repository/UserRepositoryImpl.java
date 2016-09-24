package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository;

import com.cezarykluczynski.carmen.client.github.GithubClient;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.converter.UserConverter;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.set.github.UserDTO;
import com.cezarykluczynski.carmen.util.db.TransactionalExecutor;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRepositoryImpl implements UserRepositoryCustom {

    @Autowired
    private UserRepository userRepository;

    private EntityManager entityManager;

    private GithubClient githubClient;

    private TransactionalExecutor transactionalExecutor;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager, GithubClient githubClient,
            TransactionalExecutor transactionalExecutor) {
        this.entityManager = entityManager;
        this.githubClient = githubClient;
        this.transactionalExecutor = transactionalExecutor;
    }

    @Override
    public User findUserInReportFollowersFolloweesPhase() {
        List<User> users = entityManager.createQuery("SELECT u FROM github.User u " +
                "LEFT JOIN u.userFollowers fs " +
                "LEFT JOIN u.userFollowing fg " +
                "WHERE fs.phase = :phase AND fg.phase = :phase " +
                "ORDER BY fs.updated ASC, fg.updated ASC")
                .setParameter("phase", "report")
                .setMaxResults(1)
                .getResultList();

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User create(UserDTO userDTO) {
        User userEntity = new User();
        userEntity = UserConverter.refresh(userEntity, userDTO);
        return userRepository.save(userEntity);
    }

    @Override
    public void linkFollowerWithFollowee(User follower, User followee) {
        try {
            doLinkFollowerWithFollowee(follower.getId(), followee.getId());
        } catch (Exception e) {
        }
    }

    @Override
    public User update(User userEntity, UserDTO userDTOSet) {
        userEntity = UserConverter.refresh(userEntity, userDTOSet);
        return userRepository.save(userEntity);
    }

    @Override
    public User createOrUpdateRequestedEntity(String username) throws IOException {
        HashMap<String, Boolean> flags = Maps.newHashMap();
        flags.put("requested", true);
        flags.put("optOut", false);
        return createOrUpdate(username, flags);
    }

    @Override
    public User createOrUpdateGhostEntity(String username) throws IOException {
        Map<String, Boolean> flags = Maps.newHashMap();
        flags.put("requested", false);
        flags.put("optOut", false);
        return createOrUpdate(username, flags);
    }

    @Override
    public Integer countFollowers(User user) {
        return ((BigInteger) entityManager
                .createNativeQuery("SELECT count(*) FROM github.user_followers WHERE followee_id = :followeeId")
                .setParameter("followeeId", user.getId())
                .getSingleResult()).intValue();
    }

    @Override
    public Integer countFollowees(User user) {
        List<BigInteger> resultList = entityManager
                .createNativeQuery("SELECT count(*) FROM github.user_followers WHERE follower_id = :followerId")
                .setParameter("followerId", user.getId())
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0).intValue();
    }

    @Override
    public Integer countFollowersFollowing(User user) {
        List<BigInteger> resultList = entityManager
                .createNativeQuery("SELECT count(*) FROM github.user_followers f1 " +
                        "INNER JOIN github.user_followers f2 ON f2.follower_id = f1.followee_id " +
                        "WHERE f1.follower_id = :userId AND f2.followee_id = :userId")
                .setParameter("userId", user.getId())
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0).intValue();
    }

    @Override
    public Long findHighestGitHubUserId() {
        User user = userRepository.findFirstByOrderByGithubIdDesc();
        return user == null ? null : user.getGithubId();
    }

    private User createOrUpdate(String login, Map<String, Boolean> flags) throws IOException {
        try {
            User userEntity = userRepository.findByLogin(login);
            Boolean requested = flags.get("requested");

            if (userEntity.canBeUpdated() || requested && !userEntity.isRequested()) {
                UserDTO userDTOSet = githubClient.getUser(login);
                applyFlagsToSet(userDTOSet, flags);
                return update(userEntity, userDTOSet);
            }

            return userEntity;
        } catch (NullPointerException e) {
            UserDTO userDTOSet = githubClient.getUser(login);
            applyFlagsToSet(userDTOSet, flags);
            return create(userDTOSet);
        }
    }

    private void applyFlagsToSet(UserDTO userDTOSet, Map<String, Boolean> flags) {
        if (flags.containsKey("requested")) {
            userDTOSet.setRequested(flags.get("requested"));
        }
        if (flags.containsKey("optOut")) {
            userDTOSet.setOptOut(flags.get("optOut"));
        }
    }

    private void doLinkFollowerWithFollowee(Long followerId, Long followeeId) {
        transactionalExecutor.execute(em -> {
            em
                    .createNativeQuery(
                            "INSERT INTO github.user_followers(follower_id, followee_id) " +
                                    "VALUES (:followerId, :followeeId)"
                    )
                    .setParameter("followerId", followerId)
                    .setParameter("followeeId", followeeId)
                    .executeUpdate();
            return null;
        });
    }

}

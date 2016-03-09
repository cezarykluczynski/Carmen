package com.cezarykluczynski.carmen.dao.github

import org.apache.commons.lang3.RandomStringUtils

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet

class UserDAOImplFixtures {

    private UserDAO githubUserDAOImpl

    public UserDAOImplFixtures(UserDAO githubUserDAOImpl) {
        this.githubUserDAOImpl = githubUserDAOImpl
    }

    public User createFoundRequestedUserEntity() {
        UserSet userSetEntity = getUserSetEntityWithRandomLogin()
        User userEntity = githubUserDAOImpl.create userSetEntity
        userEntity.setFound true
        userEntity.setRequested true
        githubUserDAOImpl.update userEntity
        return userEntity
    }

    public User createNotFoundRequestedUserEntity() {
        UserSet userSetEntity = getUserSetEntityWithRandomLogin()
        User userEntity = githubUserDAOImpl.create userSetEntity
        userEntity.setFound false
        userEntity.setRequested true
        githubUserDAOImpl.update userEntity
        return userEntity
    }

    public User createFoundGhostUserEntity() {
        UserSet userSetEntity = getUserSetEntityWithRandomLogin()
        User userEntity = githubUserDAOImpl.create userSetEntity
        userEntity.setFound true
        userEntity.setRequested false
        githubUserDAOImpl.update userEntity
        return userEntity
    }

    public User createNotFoundEntity() {
        String login = generateRandomLogin()
        UserSet userSet = UserSet.builder().login(login).build()
        User userEntity = githubUserDAOImpl.create userSet
        return userEntity
    }

    public String generateRandomLogin() {
        return "random_login" + System.currentTimeMillis() + RandomStringUtils.randomNumeric(10)
    }

    public void deleteUserEntity(User userEntity) {
        githubUserDAOImpl.delete userEntity
    }

    private UserSet getUserSetEntityWithRandomLogin() {
        UserSet userSetEntity = UserSet.builder().login(generateRandomLogin()).build()

        return userSetEntity
    }

}

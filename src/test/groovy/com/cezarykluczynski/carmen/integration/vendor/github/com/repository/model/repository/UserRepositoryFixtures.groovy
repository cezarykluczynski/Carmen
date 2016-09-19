package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.set.github.UserDTO as UserSet
import org.apache.commons.lang3.RandomStringUtils

class UserRepositoryFixtures {

    private UserRepository userRepository

    public UserRepositoryFixtures(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    public User createFoundRequestedUserEntity() {
        UserSet userSetEntity = getUserSetEntityWithRandomLogin()
        User userEntity = userRepository.create userSetEntity
        userEntity.setFound true
        userEntity.setRequested true
        return userRepository.save(userEntity)
    }

    public User createEntityWithGitHubUserId(Integer gitHubUserId) {
        UserSet userSetEntity = getUserSetEntityWithRandomLogin()
        User userEntity = userRepository.create userSetEntity
        userEntity.setGithubId gitHubUserId
        return userRepository.save(userEntity)
    }

    public User createNotFoundRequestedUserEntity() {
        UserSet userSetEntity = getUserSetEntityWithRandomLogin()
        User userEntity = userRepository.create userSetEntity
        userEntity.setFound false
        userEntity.setRequested true
        return userRepository.save(userEntity)
    }

    public User createFoundGhostUserEntity() {
        UserSet userSetEntity = getUserSetEntityWithRandomLogin()
        User userEntity = userRepository.create userSetEntity
        userEntity.setFound true
        userEntity.setRequested false
        userRepository.save userEntity
        return userEntity
    }

    public User createNotFoundEntity() {
        String login = generateRandomLogin()
        UserSet userSet = UserSet.builder().login(login).build()
        User userEntity = userRepository.create userSet
        return userEntity
    }

    public static String generateRandomLogin() {
        return "random_login" + System.currentTimeMillis() + RandomStringUtils.randomNumeric(10)
    }

    public void deleteUserEntity(User userEntity) {
        if (userEntity != null) {
            userRepository.delete userEntity
        }
    }

    private static UserSet getUserSetEntityWithRandomLogin() {
        UserSet userSetEntity = UserSet.builder().login(generateRandomLogin()).build()

        return userSetEntity
    }

}

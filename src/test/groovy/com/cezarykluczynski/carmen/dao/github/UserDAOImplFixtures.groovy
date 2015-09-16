package com.cezarykluczynski.carmen.dao.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.User as UserSet

@Component
class UserDAOImplFixtures {

    @Autowired
    UserDAOImpl githubUserDAOImpl

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
        Long id = 2147483647
        String login = getRandomLogin()
        UserSet userSet = new UserSet(id, login)
        User userEntity = githubUserDAOImpl.create userSet
        return userEntity
    }

    public String getRandomLogin() {
        return "random_login" + System.currentTimeMillis()
    }

    public void deleteUserEntity(User userEntity) {
        githubUserDAOImpl.delete userEntity
    }

    private UserSet getUserSetEntityWithRandomLogin() {
        UserSet userSetEntity = new UserSet(
            null,
            generateRandomLogin(),
            "",
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            false
        )

        return userSetEntity
    }

    private String generateRandomLogin() {
        return "random_login" + System.currentTimeMillis()
    }

}

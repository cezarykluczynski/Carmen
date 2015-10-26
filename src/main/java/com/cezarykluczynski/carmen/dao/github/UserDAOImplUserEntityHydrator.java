package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.User;

class UserDAOImplUserEntityHydrator {

    public UserDAOImplUserEntityHydrator() {
    }

    public User hydrate(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet) {
        userEntity.setLogin(userSet.getLogin());
        userEntity.setFound(userSet.exists());
        userEntity.setRequested(userSet.getRequested());
        userEntity.setOptOut(userSet.getOptOut());
        userEntity.setUpdated();

        if (userEntity.getFound()) {
            hydrateUserEntityUsingExistingUserSet(userEntity, userSet);
        } else {
            hydrateUserEntityWithNonExistingUser(userEntity);
        }

        return userEntity;
    }

    private void hydrateUserEntityUsingExistingUserSet(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet) {
        userEntity.setGithubId(userSet.getId());
        userEntity.setName(userSet.getName());
        userEntity.setAvatarUrl(userSet.getAvatarUrl());
        userEntity.setType(userSet.getType());
        userEntity.setSiteAdmin(userSet.getSiteAdmin());
        userEntity.setCompany(userSet.getCompany());
        userEntity.setBlog(userSet.getBlog());
        userEntity.setLocation(userSet.getLocation());
        userEntity.setEmail(userSet.getEmail());
        userEntity.setHireable(userSet.getHireable());
        userEntity.setBio(userSet.getBio());
    }

    private void hydrateUserEntityWithNonExistingUser(User userEntity) {
        userEntity.setGithubId();
        userEntity.setName("");
        userEntity.setAvatarUrl("");
        userEntity.setType("");
        userEntity.setSiteAdmin(false);
        userEntity.setCompany("");
        userEntity.setBlog("");
        userEntity.setLocation("");
        userEntity.setEmail("");
        userEntity.setHireable(false);
        userEntity.setBio("");
    }

}

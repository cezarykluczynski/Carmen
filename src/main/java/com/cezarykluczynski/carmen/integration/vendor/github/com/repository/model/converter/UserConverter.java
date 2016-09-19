package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.converter;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.set.github.UserDTO;

import java.util.Date;

public class UserConverter {

    public UserConverter() {
    }

    public static User refresh(User userEntity, UserDTO userDTO) {
        userEntity.setLogin(userDTO.getLogin());
        userEntity.setFound(userDTO.exists());
        userEntity.setRequested(userDTO.isRequested());
        userEntity.setOptOut(userDTO.isOptOut());
        userEntity.setUpdated(new Date());

        if (userEntity.isFound()) {
            refreshWithExisting(userEntity, userDTO);
        } else {
            refreshWithNonExisting(userEntity);
        }

        return userEntity;
    }

    private static void refreshWithExisting(User userEntity, UserDTO userDTO) {
        userEntity.setGithubId(userDTO.getId());
        userEntity.setName(userDTO.getName());
        userEntity.setAvatarUrl(userDTO.getAvatarUrl());
        userEntity.setType(userDTO.getType());
        userEntity.setSiteAdmin(userDTO.isSiteAdmin());
        userEntity.setCompany(userDTO.getCompany());
        userEntity.setBlog(userDTO.getBlog());
        userEntity.setLocation(userDTO.getLocation());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setHireable(userDTO.isHireable());
        userEntity.setBio(userDTO.getBio());
    }

    private static void refreshWithNonExisting(User userEntity) {
        userEntity.setGithubId(null);
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

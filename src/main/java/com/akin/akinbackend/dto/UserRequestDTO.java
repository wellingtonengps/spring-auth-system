package com.akin.akinbackend.dto;

import com.akin.akinbackend.model.User;
import com.akin.akinbackend.model.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public record UserRequestDTO(
        Long id,
        String name,
        String lastName,
        String gender,
        String email,
        @JsonIgnore String password,
        String avatar,
        Date birthDate,
        String username,
        UserRole role
) implements Serializable {

    public UserRequestDTO(User user) {
        this(user.getId(), user.getName(), user.getLastName(), user.getGender(),
                user.getEmail(), user.getPassword(), user.getAvatar(), user.getBirthDate(),
                user.getUsername(), user.getRole());
    }
}

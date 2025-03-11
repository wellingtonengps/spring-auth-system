package com.akin.akinbackend.dto;

import com.akin.akinbackend.model.User;
import com.akin.akinbackend.model.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.util.Date;

@Builder
public record UserResponseDTO(

        String name,
        String lastName,
        String gender,
        String email,
        String avatar,
        //Date birthDate,
        String username,
        String accessToken,
        String password,
        String refreshToken,
        //UserRole role,
        Boolean accountNonExpired,
        Boolean accountNonLocked,
        Boolean credentialsNonExpired,
        Boolean enabled
) {
    public User toUser() {
        User user = new User();
        user.setName(this.name);
        user.setLastName(this.lastName);
        user.setGender(this.gender);
        user.setEmail(this.email);
        user.setAvatar(this.avatar);
        //user.setBirthDate(this.birthDate);
        user.setUsername(this.username);
        user.setAccountNonExpired(this.accountNonExpired);
        user.setAccountNonLocked(this.accountNonLocked);
        user.setCredentialsNonExpired(this.credentialsNonExpired);
        user.setEnabled(this.enabled);
        user.setPassword(this.password);
        return user;
    }

}


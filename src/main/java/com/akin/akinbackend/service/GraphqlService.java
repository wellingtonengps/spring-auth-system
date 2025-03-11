package com.akin.akinbackend.service;

import com.akin.akinbackend.dto.JwtResponseDTO;
import com.akin.akinbackend.dto.UserResponseDTO;
import com.akin.akinbackend.model.User;
import com.akin.akinbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class GraphqlService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthorizationService authorizationService;

    UserDetails getUserByLogin(String username, String password) {
        return authorizationService.loadUserByUsername(username);
    }
}

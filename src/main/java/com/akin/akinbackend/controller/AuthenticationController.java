package com.akin.akinbackend.controller;

import com.akin.akinbackend.dto.*;
import com.akin.akinbackend.model.User;
import com.akin.akinbackend.repository.UserRepository;
import com.akin.akinbackend.service.AuthorizationService;
import com.akin.akinbackend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("authenticate")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequestDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = this.userRepository.findByUsername(data.username());

        if(auth.isAuthenticated()){
            return ResponseEntity.ok(UserResponseDTO.builder()
                    .accessToken(tokenService.generateToken(data.username(), 3600, "access"))
                    .refreshToken(tokenService.generateToken(data.username(), 7200, "refresh"))
                            .email(user.getEmail())
                            .lastName(user.getLastName())
                            .name(user.getName())
                            .gender(user.getGender())
                            .enabled(user.isEnabled())
                            .avatar(user.getAvatar())
                            .accountNonExpired(user.isAccountNonExpired())
                            .accountNonLocked(user.isAccountNonLocked())
                            .credentialsNonExpired(user.isCredentialsNonExpired())
                            .username(user.getUsername())
                    .build());
        } else {
            //todo: log error
            throw new RuntimeException("Error while authenticating user");
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){

        try {
            String username = tokenService.getUsername(refreshTokenRequestDTO.refreshToken());
            String tokenType = tokenService.getTokenType(refreshTokenRequestDTO.refreshToken());

            if(!tokenType.equals("refresh")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO("Error validating token: Invalid token type", HttpStatus.UNAUTHORIZED.value()));
            } else {
                return ResponseEntity.ok(JwtResponseDTO.builder()
                        .accessToken(tokenService.generateToken(username, 3600, "access"))
                        .refreshToken(tokenService.generateToken(username, 7200, "refresh"))
                        .build());
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(exception.getMessage(), HttpStatus.UNAUTHORIZED.value()));
        }
    }

}

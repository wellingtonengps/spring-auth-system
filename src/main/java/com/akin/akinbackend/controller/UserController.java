package com.akin.akinbackend.controller;

import com.akin.akinbackend.dto.UserRequestDTO;
import com.akin.akinbackend.dto.UserResponseDTO;
import com.akin.akinbackend.model.User;
import com.akin.akinbackend.repository.UserRepository;
import com.akin.akinbackend.service.TokenService;
import com.akin.akinbackend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TokenService tokenService;
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = new UserService(userRepository);
    }


    @PostMapping("/create")
    //todo: criar um DTO para receber os dados do usuário, pois o User é o model do banco de dados.
    public ResponseEntity<?> createUser(@RequestBody User user) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        var userEntity = UserResponseDTO.builder().accessToken(tokenService.generateToken(user.getUsername(), 3600, "access"))
                .refreshToken(tokenService.generateToken(user.getUsername(), 7200, "refresh"))
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
                .password(passwordEncoder.encode(user.getPassword()))
                .build();

        userService.save(userEntity.toUser());

        return ResponseEntity.ok(userEntity);
    }

    @GetMapping("/all")
    public List<UserRequestDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserRequestDTO gerUserById(@PathVariable Long id) {
        return userService.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        try {
            updateUser.setId(id);
            return userService.update(updateUser);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

}

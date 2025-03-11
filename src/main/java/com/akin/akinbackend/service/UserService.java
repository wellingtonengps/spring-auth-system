package com.akin.akinbackend.service;

import com.akin.akinbackend.dto.UserRequestDTO;
import com.akin.akinbackend.model.User;
import com.akin.akinbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<UserRequestDTO> findAll(){
        List<User> result = (List<User>) repository.findAll();
        return result.stream().map(UserRequestDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id){
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UserRequestDTO> findById(Long id){
        Optional<User> user = repository.findById(id);
        Optional<UserRequestDTO> userDTO = Optional.empty();

        if(user.isPresent()){
            userDTO = Optional.of(new UserRequestDTO(user.get()));
        }

        return userDTO;
    }

    @Transactional
    public User save(User entity){
        return repository.save(entity);
    }

    @Transactional
    public User update(User entity){
        if(!existsById(entity.getId())){
            throw new EntityNotFoundException();
        }
        return repository.save(entity);
    }

    public void delete(Long userId) {
        repository.deleteById(userId);
    }

}
package com.example.delivery_service.services;

import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    public void saveOrUpdate(User p) {
        repository.save(p);
    }

    public List<User> getAllUsers() {
        return (List<User>) repository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    public void removeUser(Long id) {
        repository.deleteById(id);
    }

    public List<User> getBySurname(String surname) {
        return repository.getBySurname(surname);
    }

    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }
}

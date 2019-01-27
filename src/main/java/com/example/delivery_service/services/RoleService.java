package com.example.delivery_service.services;

import com.example.delivery_service.model.Role;
import com.example.delivery_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }


    public void saveOrUpdate(Role r) {
        repository.save(r);
    }


    public List<Role> getAllUsers() {
        return (List<Role>) repository.findAll();
    }


    public Optional<Role> getUserById(Long id) {
        return repository.findById(id);
    }


    public void removeUser(Long id) {
        repository.deleteById(id);
    }


}

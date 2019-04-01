package com.example.delivery_service.services;

import com.example.delivery_service.model.Entity.Partner;
import com.example.delivery_service.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerService {
    private final PartnerRepository repository;

    @Autowired
    public PartnerService(PartnerRepository repository) {
        this.repository = repository;
    }

    public void saveOrUpdate(Partner p) {
        repository.save(p);
    }

    public List<Partner> getAllUsers() {
        return (List<Partner>) repository.findAll();
    }

    public Optional<Partner> getUserById(Long id) {
        return repository.findById(id);
    }

    public void removeUser(Long id) {
        repository.deleteById(id);
    }

    public Optional<Partner> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }
}

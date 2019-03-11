package com.example.delivery_service.repository;


import com.example.delivery_service.model.Entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> getBySurname(String surname);

    Optional<User> findByEmail(String email);
}

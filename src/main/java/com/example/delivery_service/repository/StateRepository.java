package com.example.delivery_service.repository;


import com.example.delivery_service.model.Entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface StateRepository extends CrudRepository<State, Long> {

    Optional<State> findByShortcut(String shortcut);
}

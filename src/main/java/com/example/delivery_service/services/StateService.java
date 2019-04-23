package com.example.delivery_service.services;

import com.example.delivery_service.model.Entity.State;
import com.example.delivery_service.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StateService {

    private final StateRepository repository;

    @Autowired
    public StateService(StateRepository repository) {
        this.repository = repository;
    }


    /*public void saveOrUpdate(State s) {
        repository.save(s);
    }*/

    public List<State> getAllStates() {
        return (List<State>) repository.findAll();
    }

    public Optional<State> getStateById(Long id) {
        return repository.findById(id);
    }

    public Optional<State> getStateByShortcut(String shortcut) {
        return repository.findByShortcut(shortcut);
    }

    /*public void removeState(Long id) {
        repository.deleteById(id);
    }*/
}

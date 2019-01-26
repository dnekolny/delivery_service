package com.example.delivery_service.services;

import com.example.delivery_service.model.Person;
import com.example.delivery_service.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonServiceImpl(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveOrUpdate(Person p) {
        repository.save(p);
    }

    @Override
    public List<Person> getAllPersons() {
        return (List<Person>) repository.findAll();
    }

    @Override
    public Optional<Person> getPersonById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removePerson(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Person> getBySurname(String surname) {
        return repository.getBySurname(surname);
    }
}

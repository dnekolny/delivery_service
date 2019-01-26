package com.example.delivery_service.services;


import com.example.delivery_service.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    public void saveOrUpdate(Person p);
    public List<Person> getAllPersons();
    public Optional<Person> getPersonById(Long id);
    public void removePerson(Long id);
    List<Person> getBySurname(String surname);
}


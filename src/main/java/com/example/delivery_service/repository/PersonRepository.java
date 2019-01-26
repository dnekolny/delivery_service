package com.example.delivery_service.repository;


import com.example.delivery_service.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> getBySurname(String surname);
}

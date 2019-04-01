package com.example.delivery_service.repository;

import com.example.delivery_service.model.Entity.Partner;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PartnerRepository extends CrudRepository<Partner, Long> {

    Optional<Partner> findByEmail(String email);
}

package com.example.delivery_service.repository;

import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.model.Entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> getByUserId(Long id);
}

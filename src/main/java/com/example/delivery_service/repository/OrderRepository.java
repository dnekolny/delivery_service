package com.example.delivery_service.repository;

import com.example.delivery_service.model.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getByUserId(Long id);

    List<Order> getByDriverIsNull();

    List<Order> getByDriver_Id(Long id);

    @Query(value = "select * from orders where ( driver_id = ?1 ) or ((driver_id is null) " +
            "and ((state = 0 and pickupType = 0) or (state = 1)))", nativeQuery = true) //"and ((state = 0 and pickupType = 0 and isInBranch = false) or (state = 1 and isInBranch = true)))", nativeQuery = true)
    List<Order> getOrdersToDeliver(Long id);
}

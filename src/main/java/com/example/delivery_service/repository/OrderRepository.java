package com.example.delivery_service.repository;

import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.model.Enums.OrderState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> getByUserId(Long id, Pageable pageable);

    List<Order> getByDriverIsNull();

    List<Order> getByDriver_Id(Long id);

    List<Order> getByDriver_IdAndStateNot(Long id, OrderState state);

    Page<Order> getByDriver_IdAndStateNot(Long id, OrderState state, Pageable pageable);

    @Query(value = "select * from orders o " +
            "join partner u on u.id = o.user_id " +
            "join partner cu on cu.id = o.customer_id " +
            "join partner r on r.id = o.recipient_id " +
            "join payment p on p.id = o.payment_id " +
            "where ((:userId is null or :userId = '' or o.user_id = :userId) and" +
            "(:owner is null or :owner = '' or u.fullname like :owner) and " +
            "(:sender is null or :sender = '' or cu.fullname like :sender) and " +
            "(:recipient is null or :recipient = '' or r.fullname like :recipient) and " +
            "((:payed is null or :payed = '') or (p.payDate is null and :payed = 'false') or (p.payDate is not null and :payed = 'true')))",
            nativeQuery = true)
    Page<Order> getAllFiltredOrders(@Param("owner") String ownerName, @Param("sender") String senderName,
                                    @Param("recipient") String recipientName, @Param("payed") String isPayed,
                                    @Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from orders where " +
            "( driver_id = ?1 and state != 3 ) or ((driver_id is null) " +
            "and ((state = 0 and pickupType = 0) or (state = 1)))", nativeQuery = true)
    List<Order> getOrdersAvailableToDeliver(Long id);

    @Query(value = "select * from orders where " +
            "( driver_id = ?1 and state != 3 ) or ((driver_id is null) " +
            "and ((state = 0 and pickupType = 0) or (state = 1)))", nativeQuery = true)
    Page<Order> getOrdersAvailableToDeliver(Long id, Pageable pageable);

    @Query(value = "select * from orders o " +
            "join partner cu on cu.id = o.customer_id " +
            "join partner r on r.id = o.recipient_id " +
            "join payment p on p.id = o.payment_id " +
            "where (( o.driver_id = :driverId and o.state != 3 ) or ((o.driver_id is null) " +
            "and ((o.state = 0 and o.pickupType = 0) or (o.state = 1)))) and " +
            "((:sender = '' or cu.fullname like :sender) and " +
            "(:recipient = '' or r.fullname like :recipient) and " +
            "((:payed = '') or (p.payDate is null and :payed = 'false') or (p.payDate is not null and :payed = 'true')))", nativeQuery = true)
    Page<Order> getFiltredOrdersAvailableToDeliver(@Param("driverId") Long driverId, @Param("sender") String senderName,
                                                   @Param("recipient") String recipientName, @Param("payed") String isPayed, Pageable pageable);
}

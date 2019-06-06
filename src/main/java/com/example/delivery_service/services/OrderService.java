package com.example.delivery_service.services;

import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final StateService stateService;

    public OrderService(OrderRepository orderRepository, StateService stateService) {
        this.repository = orderRepository;
        this.stateService = stateService;
    }

    public void saveOrUpdate(Order order) throws IOException {

        Optional<Order> origOptOrder = Optional.empty();

        if(order.getId() != null) {
            origOptOrder = getOrderById(order.getId());
        }

        /**EXIST*/
        if(origOptOrder.isPresent()){
            Order origOrder = origOptOrder.get();

            origOrder.setUpdateDates();
            //origOrder.setInBranch(order.isInBranch());

            //state
            origOrder.setState(order.getState());

            //size category
            origOrder.setSizeCategory(order.getSizeCategory());

            //pickup type
            origOrder.setPickupType(order.getPickupType());

            //payment
            if(order.getPayment().getPayDate() != null){ //paid
                if(origOrder.getPayment().getPayDate() == null)
                    origOrder.getPayment().setPayDate(new Date());
            }
            else{ //unpaid
                origOrder.getPayment().setPayDate(null);
            }
            origOrder.getPayment().setPayMethod(order.getPayment().getPayMethod());
            origOrder.countPrice();

            //customer & recipient
            origOrder.getCustomer().unite(order.getCustomer(), stateService);
            origOrder.getRecipient().unite(order.getRecipient(), stateService);

            //note
            origOrder.setNote(order.getNote());

            //driver
            origOrder.setDriver(order.getDriver());

            order = origOrder;
        }
        /**NEW*/
        else {
            //order.setInBranch(false);
            order.setCreateAndUpdateDates(null);
            order.setId(ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE));
        }

        //geocoding
        if(order.getRecipient().getAddress() != null)
            order.getRecipient().getAddress().findLatLgt();
        if(order.getCustomer().getAddress() != null)
            order.getCustomer().getAddress().findLatLgt();

        repository.save(order);
    }

    public List<Order> getAllOrders() {
        return (List<Order>) repository.findAll();
    }

    public List<Order> getWithoutDriver() {
        return repository.getByDriverIsNull();
    }

    public List<Order> getByDriverId(Long id) {
        return repository.getByDriver_Id(id);
    }

    public List<Order> getOrdersToDeliver(Long id) {
        return repository.getOrdersToDeliver(id);
    }

    public Optional<Order> getOrderById(Long id) {
        return repository.findById(id);
    }

    public void removeOrder(Long id) {
        repository.deleteById(id);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return repository.getByUserId(userId);
    }
}

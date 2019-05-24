package com.example.delivery_service.services;

import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final StateService stateService;

    public OrderService(OrderRepository orderRepository, StateService stateService) {
        this.repository = orderRepository;
        this.stateService = stateService;
    }

    public void saveOrUpdate(Order order) {

        Optional<Order> origOptOrder = Optional.empty();

        if(order.getId() != null) {
            origOptOrder = getOrderById(order.getId());
        }

        /**EXIST*/
        if(origOptOrder.isPresent()){
            Order origOrder = origOptOrder.get();
            origOrder.setUpdateDates();

            //state
            origOrder.setState(order.getState());

            //size category
            origOrder.getaPackage().setSizeCategory(order.getaPackage().getSizeCategory());

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

            order = origOrder;

            /*order.setCreateAndUpdateDates(origOptOrder.get());

            //payment
            if(origOrder.getPayment().getPayDate() == null && order.getPayment().getPayDate() != null){
                origOrder.getPayment().setPayDate(new Date());
                order.setPayment(origOrder.getPayment());
            }
            else {
                order.setPayment(origOrder.getPayment());
            }

            order.setUser(origOrder.getUser());

            //size category
            origOrder.getaPackage().setSizeCategory(order.getaPackage().getSizeCategory());
            order.setaPackage(origOrder.getaPackage());*/
        }
        /**NEW*/
        else {
            order.setCreateAndUpdateDates(null);
            order.setId(ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE));
        }

        repository.save(order);
    }

    public List<Order> getAllOrders() {
        return (List<Order>) repository.findAll();
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

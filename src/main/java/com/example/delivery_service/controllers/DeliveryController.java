package com.example.delivery_service.controllers;

import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.model.Enums.OrderState;
import com.example.delivery_service.model.Enums.PickupType;
import com.example.delivery_service.model.MapsApiKeyReader;
import com.example.delivery_service.services.OrderService;
import com.example.delivery_service.services.UserService;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Controller
public class DeliveryController {

    private final OrderService orderService;
    private final UserService userService;
    private static UserService staticUserService;

    public DeliveryController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @PostConstruct
    public void init(){
        DeliveryController.staticUserService = userService;
    }

    /**LIST*/
    @PreAuthorize("hasAnyRole('DRIVER')")
    @RequestMapping(value = "/delivery", method = RequestMethod.GET)
    public String orders(Model model, HttpServletRequest request) {

        model.addAttribute("apiKey", MapsApiKeyReader.readKey());
        model.addAttribute("orders", orderService.getOrdersByUserId(User.getCurrentUser().getId()));

        return "delivery";
    }

    /**ORDERS MAP*/
    @PreAuthorize("hasAnyRole('DRIVER')")
    @RequestMapping(value = "/orders/map", method = RequestMethod.GET)
    public String ordersMap(Model model, HttpServletRequest request) {

        model.addAttribute("apiKey", MapsApiKeyReader.readKey());
        //model.addAttribute("orders", orderService.getAllOrders());
        //model.addAttribute("orders", orderService.getOrdersWithoutDriver());
        model.addAttribute("orders", orderService.getOrdersToDeliver(User.getCurrentUser().getId()));

        return "ordersMap";
    }

    /**ADD ORDER TO LIST*/
    @PreAuthorize("hasAnyRole('DRIVER')")
    @RequestMapping(value = "/delivery/addOrderToDeliverList", method = RequestMethod.POST)
    public @ResponseBody Triplet<String, Boolean, Integer> addOrderToList(Long id, int markerId) {

        Optional<Order> optOrder = orderService.getOrderById(id);
        Optional<User> optUser = userService.getUserById(User.getCurrentUser().getId());

        boolean added = true;

        if(optUser.isPresent() && optOrder.isPresent()){
            User driver = optUser.get();

            if(driver.getRoles().iterator().next().getName().equals("DRIVER")){
                Order order = optOrder.get();

                for (Order o: driver.getDeliveryOrders()) {

                    //UN-SELECT
                    if(o.getId().equals(order.getId())){
                        //driver.getDeliveryOrders().remove(o);
                        order.setDriver(null);
                        if(order.getState() == OrderState.ON_ROAD)
                            order.setState(OrderState.PROCESSING);
                        /*else
                            order.setState(OrderState.WAITING_FOR_PACKAGE);*/
                        added = false;
                        break;
                    }
                }

                //SELECT
                if(added){
                    //driver.getDeliveryOrders().add(order);
                    if(order.getState() == OrderState.PROCESSING)
                        order.setState(OrderState.ON_ROAD);

                    order.setDriver(driver);
                }

                /*if(driver.getDeliveryOrders().contains(order))
                    driver.getDeliveryOrders().remove(order);
                else
                    driver.getDeliveryOrders().add(order);*/

                try {
                    //userService.saveOrUpdate(driver);
                    orderService.saveOrUpdate(order);
                }
                catch (IOException ex){
                    added = !added;
                }
            }
        }

        return new Triplet<>(String.valueOf(id), added, markerId);
    }

    /**DROP ORDER*/
    @PreAuthorize("hasAnyRole('DRIVER')")
    @RequestMapping(value = "/delivery/dropOrder", method = RequestMethod.POST)
    public @ResponseBody String dropOrder(Long id) {

        Optional<Order> optOrder = orderService.getOrderById(id);
        Optional<User> optUser = userService.getUserById(User.getCurrentUser().getId());

        if(optUser.isPresent() && optOrder.isPresent()){
            User driver = optUser.get();

            if(driver.getRoles().iterator().next().getName().equals("DRIVER")){
                Order order = optOrder.get();

                if(order.getDriver() != null && order.getDriver().getId().equals(driver.getId())) {

                    //drop at branch
                    if (order.getPickupType() == PickupType.PICKUP && order.getState() == OrderState.WAITING_FOR_PACKAGE) {
                        order.setState(OrderState.PROCESSING);
                        order.getPayment().setPayDate(new Date());
                        order.setDriver(null);
                    }
                    //drop to recipient
                    else if (order.getState() == OrderState.PROCESSING) {
                        order.setState(OrderState.DELIVERED);
                    }

                    try {
                        orderService.saveOrUpdate(order);
                    } catch (IOException ex) {
                        id = -1L;
                    }
                }
                else {
                    id = -1L;
                }
            }
        }

        return String.valueOf(id);
    }

    public static boolean deliveryOrdersContains(Long id){
        User user = User.getCurrentUser(staticUserService);
        if(user != null && user.getRoles().iterator().next().getName().equals("DRIVER") &&
                user.getDeliveryOrders() != null && user.getDeliveryOrders().size() > 0)
            for (Order order : user.getDeliveryOrders()) {
                if(order.getId().equals(id)) {
                    return true;
                }
            }
        return false;
    }

    /*public static String getUserDriverAddress(){
        User user = User.getCurrentUser(staticUserService);
        if(user != null && user.getRoles().iterator().next().getName().equals("DRIVER") && user.getAddress() != null)
            return user.getAddress().getFormatAddress(true);
        return "";
    }*/
}

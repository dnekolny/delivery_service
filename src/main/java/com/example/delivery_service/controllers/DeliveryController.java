package com.example.delivery_service.controllers;

import com.example.delivery_service.model.Entity.Address;
import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.model.Enums.OrderState;
import com.example.delivery_service.model.Enums.PickupType;
import com.example.delivery_service.model.GoogleMapsApi;
import com.example.delivery_service.model.MapsApiKeyReader;
import com.example.delivery_service.services.OrderService;
import com.example.delivery_service.services.StateService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class DeliveryController {

    private final OrderService orderService;
    private final UserService userService;
    private final StateService stateService;
    private static UserService staticUserService;

    public DeliveryController(OrderService orderService, UserService userService, StateService stateService, StateService stateService1) {
        this.orderService = orderService;
        this.userService = userService;
        this.stateService = stateService1;
    }

    @PostConstruct
    public void init(){
        DeliveryController.staticUserService = userService;
    }

    /**DELIVERY*/
    @PreAuthorize("hasAnyRole('DRIVER')")
    @RequestMapping(value = "/delivery", method = RequestMethod.GET)
    public String orders(Model model, HttpServletRequest request) {

        List<Order> orders = orderService.getByDriverIdWithoutDelivered(User.getCurrentUser().getId());
        String officceAddress = Address.getOfficeAddress(stateService).getFormatAddress();

        try {
            GoogleMapsApi api = new GoogleMapsApi();
            orders = api.sortOrdersByShortestRoute(officceAddress, orders);
        }
        catch (Exception ex){
            //TODO error
        }

        model.addAttribute("apiKey", MapsApiKeyReader.readKey());
        model.addAttribute("orders", orders);
        model.addAttribute("officeAddress", officceAddress);

        return "delivery";
    }

    /**ORDERS MAP*/
    @PreAuthorize("hasAnyRole('DRIVER')")
    @RequestMapping(value = "/orders/map", method = RequestMethod.GET)
    public String ordersMap(Model model, HttpServletRequest request) {

        model.addAttribute("apiKey", MapsApiKeyReader.readKey());
        model.addAttribute("orders", orderService.getOrdersAvailableToDeliver(User.getCurrentUser().getId()));

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
                catch (Exception ex){
                    added = !added;
                }
            }
        }

        return new Triplet<>(String.valueOf(id), added, markerId);
    }

    /**DROP ORDER*/
    @PreAuthorize("hasAnyRole('DRIVER')")
    @RequestMapping(value = "/delivery/dropOrder", method = RequestMethod.POST)
    public @ResponseBody Pair<String, Boolean> dropOrder(Long id) {

        Optional<Order> optOrder = orderService.getOrderById(id);
        Optional<User> optUser = userService.getUserById(User.getCurrentUser().getId());

        boolean isDelivered = false;

        if(optUser.isPresent() && optOrder.isPresent()){
            User driver = optUser.get();

            //TODO předěldělat - user nemusí být jenom DRIVER
            if(driver.getRoles().iterator().next().getName().equals("DRIVER")){
                Order order = optOrder.get();

                //WITH DRIVER
                if(order.getDriver() != null) {
                    if(order.getDriver().getId().equals(driver.getId())) {

                        //drop at branch by DRIVER
                        if (order.getState() == OrderState.WAITING_FOR_PACKAGE) {
                            order.setState(OrderState.PROCESSING);
                            if(order.getPayment().getPayDate() == null)
                                order.getPayment().setPayDate(new Date());
                            order.setDriver(null);
                        }
                        //drop to recipient by DRIVER
                        else if (order.getState() == OrderState.ON_ROAD) {
                            order.setState(OrderState.DELIVERED);
                            isDelivered = true;
                        }

                        try {
                            orderService.saveOrUpdate(order);
                        } catch (Exception ex) {
                            id = -1L;
                        }
                    }
                }
                //drop at branch by USER
                else if(order.getPickupType() == PickupType.DROP && order.getState() == OrderState.WAITING_FOR_PACKAGE) {

                    order.setState(OrderState.PROCESSING);

                    if(order.getPayment().getPayDate() == null)
                        order.getPayment().setPayDate(new Date());

                    try {
                        orderService.saveOrUpdate(order);
                    } catch (Exception ex) {
                        id = -1L;
                    }
                }
                else{
                    id = -1L;
                }
            }
        }

        return new Pair<>(String.valueOf(id), isDelivered);
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

}

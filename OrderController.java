package com.example.delivery_service.controllers;

import com.example.delivery_service.model.Entity.Address;
import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.model.Enums.OrderState;
import com.example.delivery_service.model.Entity.Payment;
import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.model.Enums.PayMethod;
import com.example.delivery_service.model.MapsApiKeyReader;
import com.example.delivery_service.services.OrderService;
import com.example.delivery_service.services.StateService;
import com.example.delivery_service.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

@Controller
public class OrderController {

    private final StateService stateService;
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(StateService stateService, OrderService orderService, UserService userService) {
        this.stateService = stateService;
        this.orderService = orderService;
        this.userService = userService;
    }

    /**LIST*/
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String orders(Model model, HttpServletRequest request) {
        if(request.isUserInRole("ADMIN")){
            model.addAttribute("apiKey", MapsApiKeyReader.readKey());
            model.addAttribute("officeAddress", Address.getOfficeAddress(stateService).getFormatAddress(true));
            model.addAttribute("orders", orderService.getAllOrders());
        }
        else if(request.isUserInRole("DRIVER")){
            model.addAttribute("apiKey", MapsApiKeyReader.readKey());
            model.addAttribute("officeAddress", Address.getOfficeAddress(stateService).getFormatAddress(true));
            model.addAttribute("orders", orderService.getOrdersToDeliver(User.getCurrentUser().getId()));
        }
        else{
            model.addAttribute("orders", orderService.getOrdersByUserId(User.getCurrentUser().getId()));
        }

        return "orders";
    }


    /**NEW*/
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/order/new", method = RequestMethod.GET)
    public String newOrder(Model model) {

        Order newOrder = new Order();
        newOrder.setUser(User.getCurrentUserFromDb(userService));
        model.addAttribute("order", newOrder);
        model.addAttribute("states", stateService.getAllStates());

        return "newOrder";
    }


    /**SUMMARY*/
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value= "/order/sum", method = RequestMethod.POST)
    public String addOrder(@Valid @ModelAttribute("order") Order order,
                           BindingResult bindingResult, Model model, HttpSession session) {

        model.addAttribute("order", order);

        if(bindingResult.hasErrors()) {
            model.addAttribute("states", stateService.getAllStates());
            return "newOrder";
        }

        //vyplní stát podle zkratky
        order.getCustomer().getAddress().fillStateFromShortcut(stateService);
        order.getRecipient().getAddress().fillStateFromShortcut(stateService);

        //nová platba - předvyplněná methoda na kartu a cenou
        Payment newPayment = new Payment();
        newPayment.setPayMethod(PayMethod.CARD);
        order.setPayment(newPayment);

        //vypočítá cenu
        //double totalPrice = order.getaPackage().getSizeCategory().getPrice() + order.getPickupType().getPrice();
        order.countPrice();

        //model.addAttribute("payment", order.getPayment());
        session.setAttribute("order", order);
        return "orderSummary";
    }

    /**ADD*/
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value= "/order/add", method = RequestMethod.POST)
    public String addOrder(HttpServletRequest request,
                           @ModelAttribute("order")Order orderWithPayment, //order obsahuje pouze payment
                           Model model, HttpSession session, RedirectAttributes redir) {

        try {
            Payment payment = orderWithPayment.getPayment();
            Order order = (Order) session.getAttribute("order");
            payment.setPrice(order.getPayment().getPrice());
            payment.fakePay();
            order.setPayment(payment);

            order.setUser(User.getCurrentUserFromDb(userService));
            order.setState(OrderState.WAITING_FOR_PACKAGE);

            orderService.saveOrUpdate(order);
        }
        catch (Exception ex){
            redir.addFlashAttribute("addErrorMessage", ex.getMessage());
        }

        return "redirect:/orders";
    }

    /**DETAIL*/
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/order/detail/{id}", method = RequestMethod.GET)
    public String detailOrder(@PathVariable("id") Long id,
                             HttpServletRequest request,
                             Model model){

        Order order = orderService.getOrderById(id).orElse(null);

        //kontrola práv
        if(order != null && (request.isUserInRole("ADMIN") || request.isUserInRole("DRIVER") ||
                User.getCurrentUser().getId().equals(order.getUser().getId()))){
            model.addAttribute("order", order);

            return "orderDetail";
        }

        return "errors/403";
    }

    /**EDIT*/
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/order/edit/{id}", method = RequestMethod.GET)
    public String editOrder(@PathVariable("id") Long id,
                            HttpServletRequest request,
                            Model model){

        Order order = orderService.getOrderById(id).orElse(null);

        model.addAttribute("states", stateService.getAllStates());
        model.addAttribute("order", order);
        return "orderEdit";
    }

    /**UPDATE*/
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/order/update", method = RequestMethod.POST)
    public String updateOrder(@Valid @ModelAttribute("order") Order order,
                             @ModelAttribute("paid") boolean isPaid,
                             HttpServletRequest request, RedirectAttributes redir){
        try {
            Payment p = new Payment();
            if (isPaid)
                p.setPayDate(new Date());
            p.setPayMethod(order.getPayment().getPayMethod());
            order.setPayment(p);

            this.orderService.saveOrUpdate(order);
        }
        catch (Exception ex){
            redir.addFlashAttribute("updateErrorMessage", ex.getMessage());
        }

        return "redirect:/order/detail/" + order.getId();
    }

    /**DELETE*/
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/order/delete/{id}", method = RequestMethod.GET)
    public String removeOrder(@PathVariable("id") Long id, HttpServletRequest request){

        this.orderService.removeOrder(id);
        return "redirect:/orders";
    }
}
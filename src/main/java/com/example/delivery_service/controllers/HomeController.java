package com.example.delivery_service.controllers;

import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.model.MapsApiKeyReader;
import com.example.delivery_service.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    private final OrderService orderService;

    @Autowired
    public HomeController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model){
        //Locale.setDefault(new Locale("en", "GB")); změna lokace
        model.addAttribute("pageTitle","Welcome to my Awesome Dynamic Application");
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request){
        if(User.getCurrentUser() != null){
            return "redirect:/orders";
        }
        model.addAttribute(new User());
        return "login";
    }

    /**FIND PACKAGE*/
    @RequestMapping(value = "/findPackage/{id}", method = RequestMethod.GET)
    public String findPackage(@PathVariable("id") Long id,
                              HttpServletRequest request,
                              Model model){

        Order order = orderService.getOrderById(id).orElse(null);

        model.addAttribute("order", order);

        return "findOrder";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String error403(){
        return "errors/403";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String error404(){
        return "errors/404";
    }

    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String map(){
        return "map";
    }
}

package com.example.delivery_service.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OrderController {
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/order/new", method = RequestMethod.GET)
    public String newOrder() {
        return "newOrder";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String orders() {
        return "orders";
    }
}

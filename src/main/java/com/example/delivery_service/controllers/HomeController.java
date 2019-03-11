package com.example.delivery_service.controllers;

import com.example.delivery_service.model.Entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model){
        //Locale.setDefault(new Locale("en", "GB")); změna lokace
        model.addAttribute("pageTitle","Welcome to my Awesome Dynamic Application");
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model){
        model.addAttribute(new User());
        return "login";
    }
}

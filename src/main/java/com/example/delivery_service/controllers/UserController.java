package com.example.delivery_service.controllers;

import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.services.RoleService;
import com.example.delivery_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String secureListUsers(Model model) {
        model.addAttribute("users", this.userService.getAllUsers());
        return "user";
    }

    //For add and update user both
    @RequestMapping(value= "/user/add", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute("user") User p, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            p.setRoles(new HashSet<>(Collections.singletonList(roleService.getRoleByName("CUSTOMER").orElse(null))));

            p.setPassword(passwordEncoder.encode(p.getPassword()));

            p.setCreateDate(new Date());
            p.setUpdateDate(p.getCreateDate());
            this.userService.saveOrUpdate(p);

            return "redirect:/users";
        }
        catch (Exception ex){
            return "redirect:/login"; //TODO přidat error messsage
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping("/remove/{id}")
    public String removeUser(@PathVariable("id") Long id){

        this.userService.removeUser(id);
        return "redirect:/users";
    }

    @RequestMapping("/edit/{id}")
    public String editUser(@Valid @ModelAttribute("user") User p){

        p.setUpdateDate(p.getCreateDate());
        this.userService.saveOrUpdate(p);
        return "redirect:/user";
    }

    @RequestMapping("/surname/{surname}")
    public String surnameTest(@PathVariable("surname") String surname, Model model){
        model.addAttribute("listUsers", this.userService.getBySurname(surname));

        return "user";
    }
}
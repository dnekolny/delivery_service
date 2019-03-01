package com.example.delivery_service.spring;

import com.example.delivery_service.model.Role;
import com.example.delivery_service.model.User;
import com.example.delivery_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class UserController {

    private final UserService userService;
    //private final

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String listUsers(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listUsers", this.userService.getAllUsers());
        return "user";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/secure/users", method = RequestMethod.GET)
    public String secureListUsers(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listUsers", this.userService.getAllUsers());
        return "user";
    }

    //For add and update user both
    @RequestMapping(value= "/user/add", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute("user") User p){

        p.setCreateDate(new Date());
        p.setUpdateDate(p.getCreateDate());
        this.userService.saveOrUpdate(p);

        return "redirect:/users";
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
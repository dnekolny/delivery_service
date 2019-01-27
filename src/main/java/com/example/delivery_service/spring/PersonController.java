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

@Controller
public class PersonController {

    private final UserService userService;
    //private final

    @Autowired
    public PersonController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public String listPersons(Model model) {
        model.addAttribute("person", new User());
        model.addAttribute("listPersons", this.userService.getAllUsers());
        return "person";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/secure/persons", method = RequestMethod.GET)
    public String secureListPersons(Model model) {
        model.addAttribute("person", new User());
        model.addAttribute("listPersons", this.userService.getAllUsers());
        return "person";
    }

    //For add and update person both
    @RequestMapping(value= "/person/add", method = RequestMethod.POST)
    public String addPerson(@Valid @ModelAttribute("person") User p){

        this.userService.saveOrUpdate(p);

        return "redirect:/persons";
    }

    @RequestMapping("/remove/{id}")
    public String removePerson(@PathVariable("id") Long id){

        this.userService.removeUser(id);
        return "redirect:/persons";
    }

    @RequestMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") Long id, Model model){
        model.addAttribute("person", this.userService.getUserById(id));
        model.addAttribute("listPersons", this.userService.getAllUsers());
        return "person";
    }

    @RequestMapping("/surname/{surname}")
    public String surnameTest(@PathVariable("surname") String surname, Model model){
        model.addAttribute("listPersons", this.userService.getBySurname(surname));

        return "person";
    }
}
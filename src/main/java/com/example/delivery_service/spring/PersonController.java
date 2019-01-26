package com.example.delivery_service.spring;

import com.example.delivery_service.model.Person;
import com.example.delivery_service.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public String listPersons(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("listPersons", this.personService.getAllPersons());
        return "person";
    }

    //For add and update person both
    @RequestMapping(value= "/person/add", method = RequestMethod.POST)
    public String addPerson(@Valid @ModelAttribute("person") Person p){

        this.personService.saveOrUpdate(p);

        return "redirect:/persons";
    }

    @RequestMapping("/remove/{id}")
    public String removePerson(@PathVariable("id") Long id){

        this.personService.removePerson(id);
        return "redirect:/persons";
    }

    @RequestMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") Long id, Model model){
        model.addAttribute("person", this.personService.getPersonById(id));
        model.addAttribute("listPersons", this.personService.getAllPersons());
        return "person";
    }

    @RequestMapping("/surname/{surname}")
    public String surnameTest(@PathVariable("surname") String surname, Model model){
        model.addAttribute("listPersons", this.personService.getBySurname(surname));

        return "person";
    }
}
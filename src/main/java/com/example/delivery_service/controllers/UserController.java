package com.example.delivery_service.controllers;

import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.model.UserDetailsImpl;
import com.example.delivery_service.services.RoleService;
import com.example.delivery_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, MessageSource messageSource) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String secureListUsers(Model model) {
        model.addAttribute("users", this.userService.getAllUsers());
        return "users";
    }

    @RequestMapping(value= "/user/add", method = RequestMethod.POST)
    public String addUser(HttpServletRequest request, @Valid @ModelAttribute("user") User user,
                          BindingResult bindingResult,
                          @ModelAttribute("autoLogin") boolean autoLogin,
                          RedirectAttributes redir) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {

            //kotroluje jestli už uživatel neexistuje
            Optional<User> exUser = userService.getUserByEmail(user.getEmail());
            if(exUser.isPresent()){
                String message = messageSource.getMessage("error.user.exist", null, LocaleContextHolder.getLocale());
                redir.addFlashAttribute("regErrorMessage", message);
                return "redirect:/login";
            }

            user.setRoles(new HashSet<>(Collections.singletonList(roleService.getRoleByName("CUSTOMER").orElse(null))));

            String pwd = user.getPassword();

            user.setPassword(passwordEncoder.encode(pwd));

            user.setCreateDate(new Date());
            user.setUpdateDate(user.getCreateDate());
            this.userService.saveOrUpdate(user);

            if(autoLogin) {
                request.login(user.getEmail(), pwd);
                return "redirect:/orders";
            }
            else
                return "redirect:/orders";
        }
        catch (Exception ex){
            String message = messageSource.getMessage("error.unexpected.while.registration", null, LocaleContextHolder.getLocale());
            redir.addFlashAttribute("regErrorMessage", message);
            return "redirect:/login"; //TODO přidat error messsage
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.GET)
    public String removeUser(@PathVariable("id") Long id){
        this.userService.removeUser(id);
        return "redirect:/users";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/user/edit/{id}", method = RequestMethod.GET)
    public String editUser(@PathVariable("id") Long id, HttpServletRequest request, Model model){

        //kontrola práv
        if(request.isUserInRole("ADMIN")){
            User user = userService.getUserById(id).orElse(null);
            model.addAttribute("user", user);
            return "userEdit";
        }
        else{
             User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(currUser.getId().equals(id)){
                model.addAttribute("user", currUser);
                return "userEdit";
            }
        }

        return "errors/403";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/user/detail/{id}", method = RequestMethod.GET)
    public String detailUser(@PathVariable("id") Long id, HttpServletRequest request, Model model){

        //kontrola práv
        if(request.isUserInRole("ADMIN")){
            User user = userService.getUserById(id).orElse(null);
            model.addAttribute("user", user);
            return "userDetail";
        }
        else{
            User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(currUser.getId().equals(id)){
                model.addAttribute("user", currUser);
                return "userDetail";
            }
        }

        return "errors/403";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public String updateUser(@Valid @ModelAttribute("user") User p, HttpServletRequest request){

        //kontrola práv
        if(request.isUserInRole("ADMIN") || ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().equals(p.getId())){
            userService.saveOrUpdate(p);
        }

        return "redirect:/user/detail/" + p.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String currentUser(Model model){

        User user = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);

        return "userDetail";
    }
}
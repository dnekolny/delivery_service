package com.example.delivery_service.model.Entity;


import com.example.delivery_service.services.UserService;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
public class User extends Partner {

    @Size(min = 8)
    @NotBlank
    private String password;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER, cascade={CascadeType.MERGE,CascadeType.REFRESH})
    private Set<Role> roles;

    @OneToMany(targetEntity = Order.class, mappedBy = "user", fetch = FetchType.EAGER)//, cascade = CascadeType.ALL) //pokud smažu uživatele smaže to i všechny jeho objednávky
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Order> orders;

    @OneToMany(targetEntity = Order.class, mappedBy = "driver", cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Order> deliveryOrders;

    private boolean isActive;

    public User() {
        super();
    }

    public User(User user) {
        super(user);
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }

    public static User getCurrentUserFromDb(UserService userService) {
        User user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            user = (User) auth.getPrincipal();

            if (user != null) {
                user = userService.getUserById(user.getId()).orElse(null);

                if(user != null) {
                    user.setPassword("");
                }
            }
        }
        return user;
    }

    public static User getCurrentUser(){
        return getCurrentUser(null);
    }

    public static User getCurrentUser(UserService userService){
        User user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            user = (User) auth.getPrincipal();

            if(userService != null && user != null){
                user = userService.getUserById(user.getId()).orElse(null);
            }

            if (user != null) {
                user.setPassword("");
            }
        }
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getDeliveryOrders() {
        return deliveryOrders;
    }

    public void setDeliveryOrders(List<Order> deliveryOrders) {
        this.deliveryOrders = deliveryOrders;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

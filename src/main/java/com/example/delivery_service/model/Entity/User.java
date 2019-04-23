package com.example.delivery_service.model.Entity;


import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;


@Entity
public class User extends Partner {

    /*@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Date createDate;

    private Date updateDate;

    @NotBlank
    private String name;*/

    @Size(min = 8)
    @NotBlank
    private String password;

    /*@Email
    @NotBlank
    private String email;

    private String phoneNumber;*/

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER, cascade={CascadeType.MERGE,CascadeType.REFRESH})
    private Set<Role> roles;

    public User() {
        super();
    }

    public User(User user) {
        super(user);
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }

    public static User getCurrentUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null) {
            user.setPassword("");
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
}

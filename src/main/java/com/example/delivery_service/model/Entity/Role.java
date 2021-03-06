package com.example.delivery_service.model.Entity;

import javax.persistence.*;
import java.util.Set;

@Entity
//@Table(name = "role")
public class Role {

    @Id
    //@Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    //@Column(name = "name")
    private String name;

    public Role() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

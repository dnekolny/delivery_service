package com.example.delivery_service.model.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Date createDate;

    private Date updateDate;

    @NotNull
    @ManyToOne(targetEntity = State.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private State state;

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String zip;

    public Address(){}

    public Address(Date createDate, Date updateDate, @NotNull State state, @NotBlank String street, @NotBlank String city, @NotBlank String zip) {
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.state = state;
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public boolean isEmpty(){
        return isEmpty(false);
    }

    //includShortcut = false -> adresa se považuje za prázdnou i když obsahuje shortcut
    //                 true -> adresa je prázdná pouze když jsou všechny parametry null nebo prázdné
    public boolean isEmpty(boolean includShortcut){
        if (includShortcut)
            return (state == null && street.isEmpty() && city.isEmpty() && zip.isEmpty());
        else
            return ((state == null || (state.getId() == null && (state.getName() == null || state.getName().isEmpty()))) && street.isEmpty() && city.isEmpty() && zip.isEmpty());
    }

    public boolean isValid(){
        return (state != null && !state.getShortcut().isEmpty() && !street.isEmpty() && !city.isEmpty() && !zip.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}

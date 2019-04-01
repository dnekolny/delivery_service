package com.example.delivery_service.model.Entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

//firma nebo osoba, které bude balík doručen
@Entity
public class Partner {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Date createDate;

    private Date updateDate;

    @NotBlank
    private String fullname;

    @Email
    private String email;

    private String phoneNumber;

    @OneToOne(targetEntity = Address.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;

    public Partner() {
    }

    public Partner(Partner partner) {
        this.id = partner.getId();
        this.createDate = partner.getCreateDate();
        this.updateDate = partner.getUpdateDate();
        this.fullname = partner.getFullname();
        this.email = partner.getEmail();
        this.phoneNumber = partner.getPhoneNumber();
        this.address = partner.getAddress();
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullName) {
        this.fullname = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

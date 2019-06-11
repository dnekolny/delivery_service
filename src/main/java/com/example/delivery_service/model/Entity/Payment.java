package com.example.delivery_service.model.Entity;

import com.example.delivery_service.model.Enums.PayMethod;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Date createDate;

    private Date updateDate;

    @Column(name = "payDate")
    private Date payDate;

    private double price;

    private PayMethod payMethod;

    public Payment() {
    }

    //simuluje zaplacení
    public void fakePay(){
        if(payMethod == PayMethod.CARD || payMethod == PayMethod.PAYPAL) {
            payDate = new Date();
        }
    }

    public void setCreateAndUpdateDates(){
        setCreateDate(new Date());
        setUpdateDate(new Date());
    }

    public void setCreateAndUpdateDates(Payment payment){
        if(payment != null)
            setCreateDate(payment.getCreateDate());
        else
            setCreateDate(new Date());
        setUpdateDate(new Date());
    }


        public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}

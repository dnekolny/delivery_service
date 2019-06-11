package com.example.delivery_service.model.Entity;

import com.example.delivery_service.model.Enums.OrderState;
import com.example.delivery_service.model.Enums.PickupType;
import com.example.delivery_service.model.Enums.SizeCategory;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "Orders")
public class Order {

    @Id
    private Long id;

    private Date createDate;

    private Date updateDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Payment payment;

    @NotNull
    private PickupType pickupType;

    private OrderState state;

    @NotNull
    private SizeCategory sizeCategory;

    private String note;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Valid
    private Partner customer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Valid
    private Partner recipient;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private User driver;

    public Order() {
        //this.aPackage = new Package();
        this.customer = new Partner();
        this.recipient = new Partner();
    }

    /**1 - balíček je připraven k doručení
     * 0 - balíček se musí vyzvednout
     * -1 - balíček nelze přepravit (nebyl předán uživatelem)*/
    public int isReadyToDeliver(){
        if (state == OrderState.ON_ROAD || state == OrderState.PROCESSING) {
            return 1;
        } else if (state == OrderState.WAITING_FOR_PACKAGE && pickupType == PickupType.PICKUP) {
            return 0;
        }
        return -1;
    }

    public void setCreateAndUpdateDates(Order order){
        if(order != null){
            setCreateDate(order.getCreateDate());
            /*if(getaPackage() != null)
                getaPackage().setCreateAndUpdateDates(order.getaPackage());*/
            if(getPayment() != null)
                getPayment().setCreateAndUpdateDates(order.getPayment());
            if(getRecipient() != null)
                getRecipient().setCreateAndUpdateDates(order.getRecipient());
            if(getCustomer() != null)
                getCustomer().setCreateAndUpdateDates(order.getCustomer());
        }
        else{
            setCreateDate(new Date());
            /*if(getaPackage() != null)
                getaPackage().setCreateAndUpdateDates(null);*/
            if(getPayment() != null)
                getPayment().setCreateAndUpdateDates(null);
            if(getRecipient() != null)
                getRecipient().setCreateAndUpdateDates(null);
            if(getCustomer() != null)
                getCustomer().setCreateAndUpdateDates(null);
        }

        setUpdateDate(new Date());
    }

    public void setUpdateDates(){
        setUpdateDate(new Date());
        /*if(aPackage != null)
            aPackage.setUpdateDate(new Date());*/
        if(payment != null)
            payment.setUpdateDate(new Date());
        if(recipient != null)
            recipient.setUpdateDates();
        if(customer != null)
            customer.setUpdateDates();
    }

    public void countPrice(){
        if(sizeCategory != null && pickupType != null && payment != null){
            payment.setPrice(sizeCategory.getPrice() + pickupType.getPrice());
        }
    }

    public String getDriveFormatAddress(){
        return getDriveAddress().getFormatAddress(true);
    }

    public Address getDriveAddress(){
        Address address = null;

        if(getPickupType() == PickupType.PICKUP && getState() == OrderState.WAITING_FOR_PACKAGE){
            //customer address
            address = getCustomer().getAddress();
        }
        else if(getState() != OrderState.DELIVERED){
            //recipient address
            address = getRecipient().getAddress();
        }

        return address;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Partner getCustomer() {
        return customer;
    }

    public void setCustomer(Partner customer) {
        this.customer = customer;
    }

    public Partner getRecipient() {
        return recipient;
    }

    public void setRecipient(Partner recipient) {
        this.recipient = recipient;
    }

    public PickupType getPickupType() {
        return pickupType;
    }

    public void setPickupType(PickupType pickupType) {
        this.pickupType = pickupType;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public SizeCategory getSizeCategory() {
        return sizeCategory;
    }

    public void setSizeCategory(SizeCategory sizeCategory) {
        this.sizeCategory = sizeCategory;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    /*public boolean isInBranch() {
        return isInBranch;
    }

    public void setInBranch(boolean inBranch) {
        isInBranch = inBranch;
    }*/
}

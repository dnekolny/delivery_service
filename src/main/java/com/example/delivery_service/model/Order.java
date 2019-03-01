package com.example.delivery_service.model;

import javax.persistence.*;
import java.util.Date;

public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Date createDate;

    private Date updateDate;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Package aPackage;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User customer;
}

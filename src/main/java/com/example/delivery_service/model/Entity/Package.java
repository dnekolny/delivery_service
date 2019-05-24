package com.example.delivery_service.model.Entity;


import com.example.delivery_service.model.Enums.SizeCategory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Package {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Date createDate;

    private Date updateDate;

    @NotNull
    private SizeCategory sizeCategory;

    private double width;
    private double height;
    private double length;

    public Package() {
    }

    public void setCreateAndUpdateDates(Package aPackage){
        if(aPackage != null)
            setCreateDate(aPackage.getCreateDate());
        else
            setCreateDate(new Date());
        setUpdateDate(new Date());
    }

    public SizeCategory getSizeCategory() {
        return sizeCategory;
    }

    public void setSizeCategory(SizeCategory sizeCategory) {
        this.sizeCategory = sizeCategory;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
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
}




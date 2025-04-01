package com.uni.bankproject.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class OtherProducts {

    @Id
    @Column(name = "product_id", unique = true, nullable = false)
    private String productid;
    private String productName;
    private String userId;
    private double price;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    public OtherProducts(String productid, String productName, String userId, double price, String description, Date createAt) {
        this.productid = productid;
        this.productName = productName;
        this.userId = userId;
        this.price = price;
        this.createAt = createAt;
    }

    public OtherProducts() {

    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}

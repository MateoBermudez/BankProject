package com.uni.bankproject.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table (name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originAccount;
    private String destinationAccount;
    private double amount;
    private String description;

    @Column (name = "transfer_date")
    @Temporal(TemporalType.DATE)
    private Date transferDate;

    public Transfer() {

    }

    public Transfer(Long id, String originAccount, String destinationAccount, double amount, String description, Date transferDate) {
        this.id = id;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.description = description;
        this.transferDate = transferDate;
    }

    public Transfer(String originAccount, String destinationAccount, double amount, String description) {
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginAccount() {
        return originAccount;
    }

    public void setOriginAccount(String originAccount) {
        this.originAccount = originAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }
}

package com.uni.bankproject.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import org.hibernate.annotations.Parameter;

import java.util.Date;
import java.util.Random;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;
    private double balance;
    private String accountType;
    private String clientId;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;


    public Account(String accountNumber, double balance, String accountType, String currency, String clientId, Date createAt) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.clientId = clientId;
        this.createAt = createAt;
    }

    public Account() {

    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}

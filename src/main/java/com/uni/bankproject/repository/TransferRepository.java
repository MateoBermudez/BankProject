package com.uni.bankproject.repository;

import com.uni.bankproject.entity.Transfer;

import java.util.List;

public interface TransferRepository {

    public boolean verifyDestinationAccount(String destinationAccount);

    public boolean verifyMoney(String originAccount, double amount);

    public void transferMoney(String originAccount, String destinationAccount, double amount);

    public void save(Transfer transfer);

    public List<Transfer> getTransfersByAccountNumber(String AccountNumber);

    public boolean VerifyTypeAccount(String id, String type);

    public boolean VerifyBalance(String id, double amount);

    public String getClientId(String id);
}

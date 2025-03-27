package com.uni.bankproject.repository;

import com.uni.bankproject.entity.Account;

import java.util.List;

public interface AccountRepository {

    public List<Account> getAccountsById(String id);

    public void CreateAccount(Account account);

    public boolean VerifyTypeAccount(String id, String type);

    public boolean DeleteAccount(Account account);

    public Account getAccountById(String id);


}

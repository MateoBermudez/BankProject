package com.uni.bankproject.service;

import com.uni.bankproject.entity.Account;
import com.uni.bankproject.entity.User;
import com.uni.bankproject.repository.AccountImplemention;
import com.uni.bankproject.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountImplemention accountImplemention;

    public String createAccount(Account account) {
        if (accountImplemention.VerifyTypeAccount(account.getClientId(), account.getAccountType())) {
            return "Ya tienes una cuenta de " + account.getAccountType();
        } else {
            accountImplemention.CreateAccount(account);
            return "Cuenta creada con éxito";
        }
    }

    public String deleteAccount(String Accountid) {
        Account account = accountImplemention.getAccountById(Accountid);
        if (account.getAccountType().equals("Savings")) {
            if (account.getBalance() > 0) {
                return "Debes de transferir el dinero a otra cuenta";
            } else {
                accountImplemention.DeleteAccount(account);
                return "Cuenta eliminada con éxito";
            }
        } else {
            if (account.getBalance() < 0) {
                return "Debes de pagar lo que debes";
            } else {
                accountImplemention.DeleteAccount(account);
                return "Cuenta eliminada con éxito";
            }

        }
    }

    public List<Account> getAccountsById(String id){
        return accountImplemention.getAccountsById(id);
    }

    public Account getAccountById(String id){
        return accountImplemention.getAccountById(id);
    }

    public String getIdByUsername (String username){
        return accountImplemention.getIdByUsername(username);
    }

    public String generateUniqueAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {
            accountNumber = String.format("%05d", random.nextInt(100000)); // Genera un número entre 00000 y 99999
        } while (accountImplemention.existsByAccountNumber(accountNumber));
        return accountNumber;
    }


}

package com.uni.bankproject.service;

import com.uni.bankproject.entity.Account;
import com.uni.bankproject.repository.AccountImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountImplementation accountImplementation;

    public String createAccount(Account account) {
        if (accountImplementation.VerifyTypeAccount(account.getClientId(), account.getAccountType())) {
            return "Ya tienes una cuenta de " + account.getAccountType();
        } else {
            if (account.getAccountType().equals("Credit")){
                if (accountImplementation.existsSavingsAccount(account.getClientId())){
                    accountImplementation.CreateAccount(account);
                    return "Cuenta creada con éxito";
                } else {
                    return "Debes de crear una cuenta de ahorros primero";
                }
            }
            else {
                accountImplementation.CreateAccount(account);
                return "Cuenta creada con éxito";
            }
        }
    }

    public String deleteAccount(String Accountid) {
        Account account = accountImplementation.getAccountById(Accountid);
        if (account.getAccountType().equals("Savings")) {
            if (account.getBalance() > 0) {
                return "Debes de transferir el dinero a otra cuenta";
            } else {
                accountImplementation.DeleteAccount(account);
                return "Cuenta eliminada con éxito";
            }
        } else {
            if (account.getBalance() < 0) {
                return "Debes de pagar lo que debes";
            } else {
                accountImplementation.DeleteAccount(account);
                return "Cuenta eliminada con éxito";
            }

        }
    }

    public List<Account> getAccountsById(String id){
        return accountImplementation.getAccountsById(id);
    }

    public Account getAccountById(String id){
        return accountImplementation.getAccountById(id);
    }

    public String getIdByUsername (String username){
        return accountImplementation.getIdByUsername(username);
    }

    public String generateUniqueAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {
            accountNumber = String.format("%05d", random.nextInt(100000)); // Genera un número entre 00000 y 99999
        } while (accountImplementation.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    public List<Account> getAllAccounts() {
        return accountImplementation.getAllAccounts();
    }


}

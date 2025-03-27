package com.uni.bankproject.repository;

import com.uni.bankproject.entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AccountImplemention implements AccountRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsById(String id) {
        String query = "SELECT a FROM Account a WHERE a.clientId = :id";
        return em.createQuery(query).setParameter("id", id).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean VerifyTypeAccount(String id, String type) {
        String query = "SELECT a FROM Account a WHERE a.clientId = :id AND a.accountType = :type";
        boolean b = em.createQuery(query).setParameter("id", id).setParameter("type", type).getResultList().size() > 0;

        return b;
    }

    @Override
    @Transactional
    public void CreateAccount(Account account) {
        em.persist(account);
    }

    @Override
    @Transactional
    public boolean DeleteAccount(Account account) {
        String query = "DELETE FROM Account a WHERE a.accountNumber = :accountNumber";
        em.createQuery(query).setParameter("accountNumber", account.getAccountNumber()).executeUpdate();
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountById(String id) {
        return em.find(Account.class, id);
    }




}

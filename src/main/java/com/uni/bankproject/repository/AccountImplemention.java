package com.uni.bankproject.repository;

import com.uni.bankproject.entity.Account;
import com.uni.bankproject.entity.User;
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

    @Override
    @Transactional(readOnly = true)
    public String getIdByUsername(String username) {
        String query = "SELECT u.userID FROM User u WHERE u.userName = :username";
        return (String) em.createQuery(query).setParameter("username", username).getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAccountNumber(String accountNumber) {
        String query = "SELECT COUNT(*) FROM Account a WHERE a.accountNumber = :accountNumber";
        Long count = (Long) em.createQuery(query).setParameter("accountNumber", accountNumber).getSingleResult();
        return count > 0;
    }




}

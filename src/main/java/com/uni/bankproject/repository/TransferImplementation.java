package com.uni.bankproject.repository;

import com.uni.bankproject.entity.Account;
import com.uni.bankproject.entity.Transfer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TransferImplementation implements TransferRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public boolean verifyDestinationAccount(String destinationAccount) {

        String query = "SELECT COUNT(*) FROM Account a WHERE a.accountNumber = :destinationAccount";
        Long count = (Long) em.createQuery(query)
                .setParameter("destinationAccount", destinationAccount).getSingleResult();

        return count > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyMoney(String originAccount, double amount) {

        String query = "SELECT a.balance FROM Account a WHERE a.accountNumber = :originAccount";
        double balance = (double) em.createQuery(query)
                .setParameter("originAccount", originAccount).getSingleResult();

        return balance >= amount;
    }

    @Override
    @Transactional
    public void transferMoney(String originAccount, String destinationAccount, double amount) {

        String query = "UPDATE Account a SET a.balance = a.balance - :amount WHERE a.accountNumber = :originAccount";
        em.createQuery(query)
                .setParameter("amount", amount)
                .setParameter("originAccount", originAccount)
                .executeUpdate();

        String query1 = "UPDATE Account a SET a.balance = a.balance + :amount WHERE a.accountNumber = :destinationAccount";
        em.createQuery(query1)
                .setParameter("amount", amount)
                .setParameter("destinationAccount", destinationAccount)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void save(Transfer transfer) {
        em.persist(transfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transfer> getTransfersByAccountNumber(String AccountNumber) {
        return em.createQuery("from Transfer t where t.originAccount = :Account OR t.destinationAccount = :Account")
                .setParameter("Account", AccountNumber)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean VerifyTypeAccount(String id, String type) {
        String query = "SELECT a FROM Account a WHERE a.accountNumber = :id AND a.accountType = :type";
        boolean b = em.createQuery(query).setParameter("id", id).setParameter("type", type).getResultList().size() > 0;
        return b;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean VerifyBalance(String id, double amount) {
        double actualBalance = (double) em.createQuery("SELECT a.balance FROM Account a WHERE a.accountNumber = :id")
                .setParameter("id", id).getSingleResult();
        if (actualBalance - amount < -2000){
            return false;
        } else {
            return true;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getClientId(String id) {
        return (String) em.createQuery("SELECT a.clientId FROM Account a WHERE a.accountNumber = :id")
                .setParameter("id", id).getSingleResult();
    }
}

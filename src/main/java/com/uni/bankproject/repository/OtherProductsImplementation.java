package com.uni.bankproject.repository;

import com.uni.bankproject.entity.OtherProducts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class OtherProductsImplementation implements OtherProductsRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProductNumber(String productid) {
        String query = "SELECT COUNT(*) FROM OtherProducts a WHERE a.productid = :productid";
        Long count = (Long) em.createQuery(query).setParameter("productid", productid).getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsbyProductName(String productType) {
        String query = "SELECT COUNT(*) FROM OtherProducts a WHERE a.productName = :productType";
        Long count = (Long) em.createQuery(query).setParameter("productType", productType).getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional
    public void createProduct(OtherProducts product) {
        em.persist(product);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean UserHasSavingAccount(String id) {
        String query = "SELECT COUNT(*) FROM Account a WHERE a.clientId = :id AND a.accountType = 'Savings'";
        Long count = (Long) em.createQuery(query).setParameter("id", id).getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public OtherProducts getProductById(String id) {
        return em.find(OtherProducts.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserandProductName(String userId, String productName) {
        String query = "SELECT COUNT(*) FROM OtherProducts a WHERE a.userId = :userId AND a.productName = :productName";
        Long count = (Long) em.createQuery(query)
                .setParameter("userId", userId)
                .setParameter("productName", productName)
                .getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public double getMoneyByUserId(String userId) {
        String query = "SELECT a.balance FROM Account a WHERE a.clientId = :userId";
        Double balance = (Double) em.createQuery(query).setParameter("userId", userId).getSingleResult();
        return balance != null ? balance : 0.0;
    }

    @Override
    @Transactional
    public void extractMoneyByUserId(String userId, double money) {
        String query = "UPDATE Account a SET a.balance = a.balance - :money WHERE a.clientId = :userId";
        em.createQuery(query)
                .setParameter("money", money)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void depositMoneyByUserId(String userId, double money) {
        String query = "UPDATE Account a SET a.balance = a.balance + :money WHERE a.clientId = :userId";
        em.createQuery(query)
                .setParameter("money", money)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteProduct(OtherProducts product) {
        em.remove(em.contains(product) ? product : em.merge(product));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean VerifyEnoughMoney(String userId, double money) {
        String query = "SELECT a.balance FROM Account a WHERE a.clientId = :userId AND a.accountType = 'Savings'";
        Double balance = (Double) em.createQuery(query).setParameter("userId", userId).getSingleResult();
        return balance != null && balance >= money;
    }

    @Override
    @Transactional(readOnly = true)
    public String getIdByUsername(String username) {
        String query = "SELECT u.userID FROM User u WHERE u.userName = :username";
        return (String) em.createQuery(query).setParameter("username", username).getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OtherProducts> getAllProductsByUserId(String userId) {
        String query = "SELECT p FROM OtherProducts p WHERE p.userId = :userId";
        return em.createQuery(query, OtherProducts.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}

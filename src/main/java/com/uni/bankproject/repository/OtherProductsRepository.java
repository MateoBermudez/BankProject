package com.uni.bankproject.repository;

import com.uni.bankproject.entity.OtherProducts;

import java.util.List;

public interface OtherProductsRepository {

    public void createProduct(OtherProducts product);

    public boolean existsByProductNumber(String productid);

    public boolean existsbyProductName(String productName);

    public boolean UserHasSavingAccount(String id);

    public OtherProducts getProductById(String id);

    public boolean existsByUserandProductName(String userId, String productName);

    public double getMoneyByUserId(String userId);

    public void extractMoneyByUserId(String userId, double money);

    public void depositMoneyByUserId(String userId, double money);

    public void deleteProduct(OtherProducts product);

    public boolean VerifyEnoughMoney(String userId, double money);

    public String getIdByUsername(String username);

    public List<OtherProducts> getAllProductsByUserId(String userId);
}

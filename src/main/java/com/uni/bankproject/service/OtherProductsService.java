package com.uni.bankproject.service;

import com.uni.bankproject.entity.OtherProducts;
import com.uni.bankproject.repository.OtherProductsImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class OtherProductsService {

    @Autowired
    private OtherProductsImplementation product;

    public String createProduct(OtherProducts otherproduct) {
        String message = "";
        if (product.UserHasSavingAccount(otherproduct.getUserId())){
            if (otherproduct.getProductName().equals("CD")){
                message = createCDT(otherproduct);
                return message;
            } else if (otherproduct.getProductName().equals("Investment")) {
                message = createInvestment(otherproduct);
                return message;
            }
            else {
                message = createLoan(otherproduct);
                return message;
            }
        }
        else {
            return "Debes de crear una cuenta de ahorros primero";
        }
    }

    public String createLoan(OtherProducts otherproduct) {
        if (!product.existsByUserandProductName(otherproduct.getUserId(), otherproduct.getProductName())) {
            double money = product.getMoneyByUserId(otherproduct.getUserId());
            if (money == 0) {
                return "No tienes dinero en tu cuenta de ahorros";
            }
            if (money * 2 < otherproduct.getPrice()){
                return "Solo puedes pedir prestado el doble de tu dinero del prestamo";
            } else {
                product.createProduct(otherproduct);
                product.depositMoneyByUserId(otherproduct.getUserId(), otherproduct.getPrice());
                return "Prestamo creado con exito";
            }
        } else {
            return "Ya tienes un prestamo creado";
        }
    }

    public String createInvestment(OtherProducts otherproduct) {
        if (!product.existsByUserandProductName(otherproduct.getUserId(), otherproduct.getProductName())) {
            double money = product.getMoneyByUserId(otherproduct.getUserId());
            if (money >= otherproduct.getPrice()){
                product.createProduct(otherproduct);
                product.extractMoneyByUserId(otherproduct.getUserId(), otherproduct.getPrice());
                return "Inversion creada con exito";
            } else {
                return "No tienes suficiente dinero para crear una inversion";
            }
        } else {
            return "Ya tienes una inversion creada";
        }

    }

    public String createCDT(OtherProducts otherproduct) {
        if (!product.existsByUserandProductName(otherproduct.getUserId(), otherproduct.getProductName())) {
            double money = product.getMoneyByUserId(otherproduct.getUserId());
            if (money >= otherproduct.getPrice()){
                product.createProduct(otherproduct);
                product.extractMoneyByUserId(otherproduct.getUserId(), otherproduct.getPrice());
                return "CDT creado con exito";
            } else {
                return "No tienes suficiente dinero para crear un CDT";
            }
        } else {
            return "Ya tienes un CDT creado";
        }
    }

    public String deleteProduct(String productid){
        OtherProducts otherproduct = product.getProductById(productid);
        String message = "";
        if (otherproduct.getProductName().equals("CD")){
            message = deleteCD(otherproduct);
            return message;
        } else if (otherproduct.getProductName().equals("Investment")) {
            message = deleteInvestment(otherproduct);
            return message;
        }
        else {
            message = deleteLoan(otherproduct);
            return message;
        }
    }

    public String deleteInvestment (OtherProducts otherproduct){
        Date date = new Date();
        LocalDate createDate = otherproduct.getCreateAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(createDate, currentDate);

        double formula = (daysBetween * 0.001);
        double money = otherproduct.getPrice() + (otherproduct.getPrice() * formula);
        product.depositMoneyByUserId(otherproduct.getUserId(), money);
        product.deleteProduct(otherproduct);
        return "Inversion eliminada con exito";
    }

    public String deleteCD (OtherProducts otherproduct){
        Date date = new Date();
        LocalDate createDate = otherproduct.getCreateAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(createDate, currentDate);

        if (daysBetween < 30){
            return "El tiempo minimo de un CD es de 30 dias";
        } else {
            double formula = (daysBetween * 0.05) / 30;
            double money = otherproduct.getPrice() + (otherproduct.getPrice() * formula);
            product.depositMoneyByUserId(otherproduct.getUserId(), money);
            product.deleteProduct(otherproduct);
            return "CD eliminado con exito";
        }
    }

    public String deleteLoan (OtherProducts otherproduct){
        Date date = new Date();
        LocalDate createDate = otherproduct.getCreateAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(createDate, currentDate);
        double formula = (daysBetween * 0.05) / 30;
        double money = otherproduct.getPrice() + (otherproduct.getPrice() * formula);
        if (product.VerifyEnoughMoney(otherproduct.getUserId(), money)){
            product.extractMoneyByUserId(otherproduct.getUserId(), money);
            product.deleteProduct(otherproduct);
            return "Prestamo Pagado con exito";
        } else {
            return "No tienes suficiente dinero para pagar el préstamo en tu cuenta de ahorros";
        }
    }

    public String getIdByUsername(String username) {
        return product.getIdByUsername(username);
    }

    public String generateUniqueProductNumber() {
        Random random = new Random();
        String productnumber;
        do {
            productnumber = String.format("%05d", random.nextInt(100000));
        } while (product.existsByProductNumber(productnumber));
        return productnumber;
    }

    public List<OtherProducts> getProductsById(String id){
        return product.getAllProductsByUserId(id);
    }

}

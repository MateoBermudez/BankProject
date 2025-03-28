package com.uni.bankproject.controller;

import com.uni.bankproject.entity.Account;
import com.uni.bankproject.entity.User;
import com.uni.bankproject.service.AccountService;
import com.uni.bankproject.utils.CookieUtils;
import com.uni.bankproject.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;

@Controller
public class AccountController {

    private final AccountService accountService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AccountController(AccountService accountService, JwtUtils jwtUtils) {
        this.accountService = accountService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/deleteAccount/{id}")
    public String deleteAccount(@PathVariable String id, Model model){
        Account account = accountService.getAccountById(id);
        String deleteMessage = accountService.deleteAccount(id);
        model.addAttribute("titulo", "Eliminar cuenta");
        model.addAttribute("Userid", account.getClientId());
        model.addAttribute("deleteMessage", deleteMessage);
        return "deleteAccount";
    }

    @GetMapping("/getAccounts")
    public String getAccounts(Model model, HttpServletRequest request){
        String token = CookieUtils.getCookieValue(request.getCookies(), "jwt");
        if (token != null) {
            String username = jwtUtils.getUsernameFromToken(token);
            String userid = accountService.getIdByUsername(username);
            model.addAttribute("titulo", "Listado de cuentas");
            model.addAttribute("Userid", userid);
            model.addAttribute("accounts", accountService.getAccountsById(userid));
        } else {
            return "redirect:/login";
        }
        return "getAccounts";
    }


    @GetMapping("/createAccount")
    public String createAccount(Model model, HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request.getCookies(), "jwt");
        if (token != null) {
            model.addAttribute("titulo", "Crear cuenta");
            String username = jwtUtils.getUsernameFromToken(token);
            String userId = accountService.getIdByUsername(username);
            model.addAttribute("userId", userId);
            Account account = new Account();
            account.setClientId(userId);
            account.setAccountNumber(accountService.generateUniqueAccountNumber());
            model.addAttribute("account", account);
        }
        return "createAccount";
    }

    @PostMapping("/createAccount")
    public String createAccount(@ModelAttribute Account account, Model model, HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request.getCookies(), "jwt");
        if (token != null){
            String username = jwtUtils.getUsernameFromToken(token);
            String userId = accountService.getIdByUsername(username);
            account.setClientId(userId);
            account.setCreateAt(new Date());
            account.setAccountNumber(accountService.generateUniqueAccountNumber());
            String message = accountService.createAccount(account);
            model.addAttribute("message", message);
        } else {
            return "redirect:/login";
        }
        return "createAccount";
    }
}

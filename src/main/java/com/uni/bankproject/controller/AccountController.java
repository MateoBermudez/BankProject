package com.uni.bankproject.controller;

import com.uni.bankproject.entity.Account;
import com.uni.bankproject.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/createAccount/{clientId}")
    public String createAccount(@PathVariable String clientId, Model model) {
        model.addAttribute("titulo", "Crear cuenta");
        Account account = new Account();
        account.setClientId(clientId);
        model.addAttribute("account", account);
        return "createAccount";
    }

    @PostMapping("/createAccount")
    public String createAccount(@ModelAttribute Account account, Model model) {
        account.setCreateAt(new Date());
        String message = accountService.createAccount(account);
        model.addAttribute("message", message);
        return "createAccount";
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

    @GetMapping("/getAccounts/{userid}")
    public String getAccounts(@PathVariable String userid, Model model){
        model.addAttribute("titulo", "Listado de cuentas");
        model.addAttribute("Userid", userid);
        model.addAttribute("accounts", accountService.getAccountsById(userid));
        return "getAccounts";
    }
}

package com.uni.bankproject.controller;

import com.uni.bankproject.entity.Account;
import com.uni.bankproject.entity.Transfer;
import com.uni.bankproject.service.AccountService;
import com.uni.bankproject.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;


    @GetMapping("/transfer/{originAccount}")
    public String transferForm(Model model, @PathVariable String originAccount) {
        Transfer transfer = new Transfer();
        transfer.setOriginAccount(originAccount);
        model.addAttribute("titulo", "Transferencia");
        model.addAttribute("transfer", transfer);
        model.addAttribute("originAccount", originAccount);

        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@ModelAttribute Transfer transfer, Model model) {
        transfer.setTransferDate(new Date());
        String message = transferService.transferMoney(transfer);
        model.addAttribute("message", message);
        return "transfer";
    }

    @GetMapping("/getTransfers/{id}")
    public String getTransfers(Model model, @PathVariable String id) {

        model.addAttribute("titulo", "Listado de Transferencias");
        model.addAttribute("transfers", transferService.getTransfersByAccount(id));
        model.addAttribute("account", id);
        return "getTransfers";
    }



}

package com.uni.bankproject.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.entity.Account;
import com.uni.bankproject.service.AccountService;
import com.uni.bankproject.service.UserService;
import com.uni.bankproject.utils.CookieUtils;
import com.uni.bankproject.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class AccountController {

    private final AccountService accountService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    public AccountController(AccountService accountService, JwtUtils jwtUtils, UserService userService) {
        this.accountService = accountService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
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

    @GetMapping("/account/downloadPdf")
    public void downloadPdf(HttpServletResponse response, @CookieValue(value = "jwt") String token) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=accounts.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        Paragraph title = new Paragraph("Your Bank Account Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("""
                
                Welcome to your detailed bank account report. This document provides a comprehensive overview of your financial activities and account information. 
                Below, you will find essential details about your account, including the account number, balance, account type, and the date of creation. 
                This report is designed to give you a clear and concise snapshot of your financial status, helping you manage your finances effectively.
                
                Account Information:
                
                Owner ID: The unique identifier of the account owner.
                Account Number: Your specific account number for transactions.
                Balance: The current balance available in your account.
                Account Type: The type of account you hold (e.g., Savings, Checking).
                Created At: The date when your account was created.
                We hope this report helps you stay informed about your financial status. 
                For any questions or further assistance, please contact our customer support.
                
                """, cellFont));

        UserDTO user = userService.getUserById(token);
        List<Account> accounts = accountService.getAccountsById(user.getUserID());

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {1f, 2f, 1f, 1f, 2f};
        table.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Phrase("Owner ID", headerFont));
        PdfPCell cell2 = new PdfPCell(new Phrase("Account Number", headerFont));
        PdfPCell cell3 = new PdfPCell(new Phrase("Balance", headerFont));
        PdfPCell cell4 = new PdfPCell(new Phrase("Account Type", headerFont));
        PdfPCell cell5 = new PdfPCell(new Phrase("Created At", headerFont));

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);

        for (Account account : accounts) {
            table.addCell(new PdfPCell(new Phrase(account.getClientId(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(account.getAccountNumber(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(account.getBalance()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(account.getAccountType(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(account.getCreateAt().toString(), cellFont)));
        }

        Paragraph contactInfo = new Paragraph("""
                
                University Bank
                Address: 123 University St, City, State, ZIP
                Phone: (123) 456-7890
                
                """, cellFont);
        contactInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(contactInfo);

        document.add(table);
        document.close();
    }
}

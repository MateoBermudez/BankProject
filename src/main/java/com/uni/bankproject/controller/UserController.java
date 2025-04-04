package com.uni.bankproject.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.entity.Account;
import com.uni.bankproject.entity.OtherProducts;
import com.uni.bankproject.service.AccountService;
import com.uni.bankproject.service.UserService;
import com.uni.bankproject.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, AccountService accountService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.accountService = accountService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/home-admin")
    public String home(Model model, @CookieValue(value = "jwt", defaultValue = "") String token) {
        List<UserDTO> users = userService.getAllUsers(token);
        model.addAttribute("users", users);
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "home-admin";
    }

    @GetMapping
    public String getUserById(@CookieValue(value = "jwt", defaultValue = "") String token, Model model) {
        UserDTO user = userService.getUserById(token);
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/delete")
    public String deleteUser(@CookieValue(value = "jwt", defaultValue = "") String token) {
        userService.deleteUser(token);
        return "redirect:/api/v1/auth/register";
    }

    @PostMapping("/update")
    public String updateUser(@CookieValue(value = "jwt", defaultValue = "") String token, @ModelAttribute UserDTO userDTO) {
        userService.updateUser(token, userDTO);
        return "redirect:/api/v1/user";
    }

    @PostMapping("/change-password")
    public String changePassword(@CookieValue(value = "jwt", defaultValue = "") String token,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword) {
        userService.changePassword(token, currentPassword, newPassword, confirmPassword);
        return "redirect:/api/v1/user";
    }

    @GetMapping("/admin/downloadPdf-users")
    public void downloadPdfUsers(HttpServletResponse response, @CookieValue(value = "jwt") String token) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=users.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        Paragraph title = new Paragraph("User Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\nThis document provides a comprehensive overview of all users.\n\n", cellFont));

        List<UserDTO> users = userService.getAllUsers(token);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {2f, 2f, 2f, 2f, 2f};
        table.setWidths(columnWidths);

        table.addCell(new PdfPCell(new Phrase("User ID", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Username", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Email", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Phone Number", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Address", headerFont)));

        for (UserDTO user : users) {
            table.addCell(new PdfPCell(new Phrase(user.getUserID(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(user.getUserName(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(user.getEmail(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(user.getPhoneNumber(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(user.getAddress(), cellFont)));
        }

        document.add(table);

        Paragraph contactInfo = new Paragraph("\nUniversity Bank\nAddress: 123 University St, City, State, ZIP\nPhone: (123) 456-7890\n", cellFont);
        contactInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(contactInfo);

        document.close();
    }

    @GetMapping("/admin/downloadPdf-accounts")
    public void downloadPdfAccount(HttpServletResponse response, @CookieValue(value = "jwt") String token) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=accounts.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        Paragraph title = new Paragraph("Account Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\nThis document provides a comprehensive overview of all accounts.\n\n", cellFont));

        String username = jwtUtils.getUsernameFromToken(token);
        userService.checkIfUserIsAdmin(username);
        List<Account> accounts = accountService.getAllAccounts();

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {2f, 1f, 1f, 2f, 2f};
        table.setWidths(columnWidths);

        table.addCell(new PdfPCell(new Phrase("Account Number", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Balance", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Account Type", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Client ID", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Created At", headerFont)));

        for (Account account : accounts) {
            table.addCell(new PdfPCell(new Phrase(account.getAccountNumber(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(account.getBalance()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(account.getAccountType(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(account.getClientId(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(account.getCreateAt() != null ? account.getCreateAt().toString() : "N/A", cellFont)));
        }

        document.add(table);

        Paragraph contactInfo = new Paragraph("\nUniversity Bank\nAddress: 123 University St, City, State, ZIP\nPhone: (123) 456-7890\n", cellFont);
        contactInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(contactInfo);

        document.close();
    }
}

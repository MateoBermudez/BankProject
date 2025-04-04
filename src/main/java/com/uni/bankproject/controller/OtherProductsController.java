package com.uni.bankproject.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.entity.OtherProducts;
import com.uni.bankproject.service.OtherProductsService;
import com.uni.bankproject.service.UserService;
import com.uni.bankproject.utils.CookieUtils;
import com.uni.bankproject.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class OtherProductsController {

    private final OtherProductsService otherProductsService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    public OtherProductsController(OtherProductsService otherProductsService, JwtUtils jwtUtils, UserService userService) {
        this.otherProductsService = otherProductsService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping("/createOtherProduct")
    public String createOtherProduct(Model model, HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request.getCookies(), "jwt");
        if (token != null) {
            String username = jwtUtils.getUsernameFromToken(token);
            String userId = otherProductsService.getIdByUsername(username);
            model.addAttribute("titulo", "Crear otro producto");
            model.addAttribute("userId", userId);
            OtherProducts otherProduct = new OtherProducts();
            otherProduct.setUserId(userId);
            otherProduct.setProductid(otherProductsService.generateUniqueProductNumber());
            model.addAttribute("otherProduct", otherProduct);
            return "createOtherProduct";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/createOtherProduct")
    public String createOtherProductPost(Model model, HttpServletRequest request, OtherProducts otherProduct) {
        String token = CookieUtils.getCookieValue(request.getCookies(), "jwt");
        if (token != null) {
            String username = jwtUtils.getUsernameFromToken(token);
            String userid = otherProductsService.getIdByUsername(username);
            otherProduct.setUserId(userid);
            otherProduct.setCreateAt(new Date());
            otherProduct.setProductid(otherProductsService.generateUniqueProductNumber());
            String message = otherProductsService.createProduct(otherProduct);
            model.addAttribute("message", message);
            model.addAttribute("otherProduct", otherProduct);
            return "createOtherProduct";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/getOtherProducts")
    public String getOtherProducts(Model model, HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request.getCookies(), "jwt");
        if (token != null) {
            String username = jwtUtils.getUsernameFromToken(token);
            String userid = otherProductsService.getIdByUsername(username);
            model.addAttribute("titulo", "Listado de otros productos");
            model.addAttribute("Userid", userid);
            model.addAttribute("otherProducts", otherProductsService.getProductsById(userid));
            return "getOtherProducts";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/deleteOtherProduct/{productid}")
    public String deleteOtherProduct(@PathVariable String productid, Model model) {
        model.addAttribute("titulo", "Eliminar producto");
        String message = otherProductsService.deleteProduct(productid);
        model.addAttribute("message", message);
        return "deleteOtherProduct";
    }

    @GetMapping("/product/downloadPdf")
    public void downloadPdf(HttpServletResponse response, @CookieValue(value = "jwt") String token) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=product.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        Paragraph title = new Paragraph("Your Bank Product Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("""

                Welcome to your detailed bank Product report. This document provides a comprehensive overview of your financial activities and products information.
                Below, you will find essential details about your product, including the product number, balance, product type, and the date of creation.
                This report is designed to give you a clear and concise snapshot of your financial status, helping you manage your finances effectively.

                Product Information:

                Owner ID: The unique identifier of the product owner.
                Product Number: Your specific product number for transactions.
                Balance: The current balance in your product.
                Product Type: The type of product you hold (e.g., Loan, CDT, Investment).
                Created At: The date when your product was created.
                We hope this report helps you stay informed about your financial status.
                For any questions or further assistance, please contact our customer support.

                """, cellFont));

        UserDTO user = userService.getUserById(token);
        List<OtherProducts> products = otherProductsService.getProductsById(user.getUserID());

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {1f, 2f, 1f, 1f, 2f};
        table.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Phrase("Owner ID", headerFont));
        PdfPCell cell2 = new PdfPCell(new Phrase("Product Number", headerFont));
        PdfPCell cell3 = new PdfPCell(new Phrase("Balance", headerFont));
        PdfPCell cell4 = new PdfPCell(new Phrase("Product Type", headerFont));
        PdfPCell cell5 = new PdfPCell(new Phrase("Created At", headerFont));

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);

        for (OtherProducts product : products) {
            table.addCell(new PdfPCell(new Phrase(product.getUserId(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(product.getProductid(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(product.getPrice()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(product.getProductName(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(product.getCreateAt().toString(), cellFont)));
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

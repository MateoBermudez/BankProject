package com.uni.bankproject.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uni.bankproject.entity.Transfer;
import com.uni.bankproject.service.TransferService;
import com.uni.bankproject.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;
    @Autowired
    private UserService userService;


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
        String clientId = transferService.getClientId(id);
        model.addAttribute("titulo", "Listado de Transferencias");
        model.addAttribute("transfers", transferService.getTransfersByAccount(id));
        model.addAttribute("clientId", clientId);
        model.addAttribute("id", id);
        return "getTransfers";
    }


    @GetMapping("/transfer/downloadPdf/{id}")
    public void downloadPdf(HttpServletResponse response, @PathVariable String id) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=transfer.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        Paragraph title = new Paragraph("Transfer Report - Account: " + id, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("""

                Welcome to your detailed transfer report. This document provides a comprehensive overview of your transfer activities.
                Below, you will find essential details about your transfers, including the origin account, destination account, amount, description, and the date of transfer.
                This report is designed to give you a clear and concise snapshot of your transfer history, helping you manage your finances effectively.

                Transfer Information:

                ID: The unique identifier of the transfer.
                Origin Account: The account from which the transfer was made.
                Destination Account: The account to which the transfer was made.
                Amount: The amount transferred.
                Description: The description of the transfer.
                Transfer Date: The date when the transfer was made.
                We hope this report helps you stay informed about your transfer activities.
                For any questions or further assistance, please contact our customer support.

                """, cellFont));

        List<Transfer> transfers = transferService.getTransfersByAccount(id);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {1f, 2f, 2f, 1f, 2f, 2f};
        table.setWidths(columnWidths);

        table.addCell(new PdfPCell(new Phrase("ID", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Origin Account", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Destination Account", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Amount", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Description", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Transfer Date", headerFont)));

        for (Transfer transfer : transfers) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(transfer.getId()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(transfer.getOriginAccount(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(transfer.getDestinationAccount(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(transfer.getAmount()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(transfer.getDescription(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(transfer.getTransferDate().toString(), cellFont)));
        }

        document.add(table);

        Paragraph contactInfo = new Paragraph("""

                University Bank
                Address: 123 University St, City, State, ZIP
                Phone: (123) 456-7890

                """, cellFont);
        contactInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(contactInfo);

        document.close();
    }

}

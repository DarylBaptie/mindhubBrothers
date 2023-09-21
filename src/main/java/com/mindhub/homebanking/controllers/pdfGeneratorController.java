package com.mindhub.homebanking.controllers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
public class pdfGeneratorController {

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/api/clients/current/transactions/pdf")
    public void generatePDF(HttpServletResponse httpServletResponse, Authentication authentication, @RequestParam long id) throws IOException {

        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findAccountById(id);
        List<TransactionDTO> transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(toList());


//        Stream<TransactionDTO> finalTransactions = transactions.stream().filter(transactionDTO -> transactionDTO.getDate().isAfter(date1) && transactionDTO.getDate().isBefore(date2));

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, httpServletResponse.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Account Transactions", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        Paragraph paragraph2 = new Paragraph();
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);


        PdfPTable table = new PdfPTable(4);

        PdfPCell hcell;

        hcell = new PdfPCell(new Phrase("Type", fontTitle));
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Amount", fontTitle));
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Description", fontTitle));
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("date", fontTitle));
        table.addCell(hcell);




        for (TransactionDTO transactionDTO : transactions) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(String.valueOf(transactionDTO.getType())));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(transactionDTO.getAmount())));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(transactionDTO.getDescription()));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(transactionDTO.getDate())));
            table.addCell(cell);

        }

        document.add(paragraph);
        document.add(table);
        document.close();

        httpServletResponse.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        httpServletResponse.setHeader(headerKey, headerValue);



        pdfGeneratorService.exportPDF(httpServletResponse);
    }
}

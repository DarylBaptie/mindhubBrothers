package com.mindhub.homebanking.services.implement;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.services.PdfGeneratorService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class PdfGeneratorServiceImplement implements PdfGeneratorService {

    @Override
    public void exportPDF(HttpServletResponse httpServletResponse) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, httpServletResponse.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("This is the title", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        Paragraph paragraph2 = new Paragraph("This is a paragraph", fontParagraph);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph);
        document.add(paragraph2);
        document.close();

    }
}

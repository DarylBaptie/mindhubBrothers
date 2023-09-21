package com.mindhub.homebanking.services;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PdfGeneratorService {

    public void exportPDF(HttpServletResponse httpServletResponse) throws IOException;
}

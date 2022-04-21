package com.finki.library.service;

import com.finki.library.model.User;
import com.lowagie.text.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public interface PDFExporterService {
    public String convertCyrilic(String message);

    public void export(HttpServletResponse response, User user,Long bookId)throws DocumentException, IOException;
}

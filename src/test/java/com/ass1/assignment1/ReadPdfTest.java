/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ass1.assignment1;

import com.ass1.pdf.ReadPdf;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author hamado
 */
public class ReadPdfTest {

    @Test
    public void readPdf() throws IOException, Exception {
        Path tempFilePath = Files.createTempFile("prova", ".txt");
        File tempFile = new File(tempFilePath.toString());
        createNewPdf();

        File pdfFIle = new File(this.getClass().getClassLoader().getResource("prova.pdf").getFile());

        ReadPdf PDF = new ReadPdf();
        PDF.readPdf(pdfFIle.getAbsolutePath());

        assertTrue(tempFile.exists());
    }

    public void createNewPdf() throws IOException {

        try (PDDocument doc = new PDDocument()) {
            PDPage myPage = new PDPage();
            doc.addPage(myPage);

            try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {
                cont.beginText();
                cont.setFont(PDType1Font.TIMES_ROMAN, 12);
                cont.setLeading(14.5f);
                cont.newLineAtOffset(25, 700);
                String line1 = "prova1";
                cont.showText(line1);

                cont.newLine();
                String line2 = "prova2";
                cont.showText(line2);

                cont.newLine();
                String line3 = "prova3";
                cont.showText(line3);

                cont.newLine();
                String line4 = "prova4";
                cont.showText(line4);

                cont.newLine();
                String line5 = "prova5";
                cont.showText(line5);
                cont.endText();
            }
            for (int i = 0; i < 50; i++) {
                doc.save("src/main/resources/prova" + i + ".pdf");
            }
        }
    }
}

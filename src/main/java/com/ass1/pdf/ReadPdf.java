package com.ass1.pdf;

import java.io.File;
import java.util.Optional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

/**
 *
 * @author Hamado Dene
 */
public class ReadPdf {
    
    //Simple function to read a pdf file
    //This is a test
    public void readPdf(String fileName) throws Exception {

        File file = new File(fileName);

        if (!file.exists()) {
            throw new Exception("File " + fileName + " not exist");
        } else {

            String fileExtension = getExtensionByStringHandling(file.getName());

            if (!"pdf".equals(fileExtension)) {
                System.out.println("Skipping no pdf file:  " + file.getName());
            } else {

                try (PDDocument document = PDDocument.load(file)) {
                    document.getClass();

                    if (!document.isEncrypted()) {
                        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                        stripper.setSortByPosition(true);

                        PDFTextStripper tStripper = new PDFTextStripper();

                        String pdfFIleInText = tStripper.getText(document);

                        String lines[] = pdfFIleInText.split("\\r?\\n");
                        for (String line : lines) {
                            System.out.println(line);
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        }

    }

    public String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse("");
    }

}

package com.ass1.assignment1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author Hamado Dene
 */
public class Worker extends Thread {    
    private static ArrayList<String> allFiles = new ArrayList<String>();
    private PDDocument document;
    private final Monitor monitor;
    
    public Worker(Monitor monitor) {
       this.monitor = monitor;
    }
    
    public void run(){
        //Start work
    }
    
    void parsePdf(File file) {
        try {
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfFIleInText = stripper.getText(document);            
            String words[] = pdfFIleInText.split("\\r?\\n");
            
            for (String word : words) {
                monitor.updateOccurences(word);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong " + e);
        }
    }
    
    
}

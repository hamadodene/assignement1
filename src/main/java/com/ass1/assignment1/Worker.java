/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ass1.assignment1;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author Hamado Dene
 */
public class Worker extends Thread {
    private PDDocument document;
    private final Monitor monitor;
    private final String directory;
    
    public Worker(Monitor monitor , String directory) {
       this.monitor = monitor;
       this.directory = directory;
    }
    
    public void run(){
        
        
    }
    
    void parsePdf(File file) {
        try {
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfFIleInText = stripper.getText(document);            
            String words[] = pdfFIleInText.split("\\r?\\n");
            
            for (String word : words) {
                monitor.update(word);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong " + e);
        }
    }
    
}

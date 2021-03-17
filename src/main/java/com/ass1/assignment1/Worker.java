package com.ass1.assignment1;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ass1.assignment1.exception.ForcedStopException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * @author Hamado Dene
 */
public class Worker extends Thread {

    private PDDocument document;
    private final Monitor monitor;
    private int numberOfRecordProcessed = 0;
    private boolean verbose = Boolean.getBoolean("debug");

    public Worker(final String name, Monitor monitor) {
        super(name);
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (monitor.existNextFile()) {
            try {
                long _start = System.currentTimeMillis();
                File file = monitor.getNextFile();
                if (file != null) {
                    System.out.println(this.getName() + ": " + "Parsing pdf " + file.getName());
                    //Parse pdf
                    parsePdf(file);
                    long _stop = System.currentTimeMillis();
                    long t = _stop - _start;
                    System.out.println("Parse pdf " + file.getName() + " in " + t + " ms");
                    System.out.println(this.getName() + " processed actually " + numberOfRecordProcessed + " words");
                }
            } catch (InterruptedException ex) {
                System.out.println("Something went wrong, please retry");
                ex.printStackTrace();
            }
        }
        System.out.println(this.getName() +": " + "Nothing to do, i go sleep");
    }

    /**
     *
     * @param file
     * @throws InterruptedException
     *
     * Parse pdf
     */
    private void parsePdf(File file) throws InterruptedException {
        try {
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfFIleInText = stripper.getText(document);
            String words[] = pdfFIleInText.split("\\W+");
            List<String> exclusion = monitor.wordsToExclude();

            for (String word : words) {
                if(!exclusion.contains(word)) {
                    //update occurrences
                    numberOfRecordProcessed = monitor.updateOccurrence(word);
                } else if(verbose){
                    System.out.println(this.getName() +": " + "Exclude word " + word);
                }
            }
            document.close();
        } catch (IOException | ForcedStopException ex) {
            System.out.println( this.getName() + ": " + "Something went wrong, please check " + ex);
        }
    }
}

package com.ass1.assignment1;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String,Integer> occurrences;
    private final int DEFAULT_WORD_COUNT = 1;
    private int NumberOfRecordProcessed = 0;

    public Worker(final String name, Monitor monitor) {
        super(name);
        this.monitor = monitor;
        occurrences = new HashMap<>();
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
                    System.out.println("Processed pdf " + file.getName() + " in " + t + " ms");
                }
            } catch (InterruptedException ex) {
                System.out.println("Something went wrong: " + ex.getMessage());
            }
        }
        if(!occurrences.isEmpty()) {
            //Update global Map
            monitor.updateOccurrence(occurrences, this.getName());
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
                if(!exclusion.contains(word.toLowerCase())) {
                    addOccurrences(word);
                    System.out.println(this.getName() + ": Processed actually " + numberOfRecordProcessed + " words " + "occurrences " +occurrences.size());
                } else {
                    System.out.println(this.getName() +": " + "Exclude word " + word);
                }
            }
            document.close();
        } catch (IOException ex) {
            System.out.println( this.getName() + ": " + "Something went wrong, please check " + ex.getMessage());
        }
    }

    public void addOccurrences(String word) {
        if(occurrences.containsKey(word)){
            int value = occurrences.get(word);
            occurrences.put(word, value + 1);
        } else {
            occurrences.put(word, DEFAULT_WORD_COUNT);
            numberOfRecordProcessed++;
        }
    }
}

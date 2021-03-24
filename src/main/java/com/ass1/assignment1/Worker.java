package com.ass1.assignment1;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.ass1.assignment1.exception.ForcedStopException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * @author Hamado Dene
 * Worker class
 */
public class Worker extends Thread {

    private PDDocument document;
    private final Monitor monitor;
    private int numberOfRecordProcessed = 0;
    private Map<String,Integer> occurrences;
    private final int DEFAULT_WORD_COUNT = 1;

    public Worker(final String name, Monitor monitor) {
        super(name);
        this.monitor = monitor;
        occurrences = new HashMap<>();
    }

    @Override
    public void run() {
        while (monitor.existNextFile() && !running()) {
            try {
                long _start = System.currentTimeMillis();
                File file = monitor.getNextFile(this.getName());
                if (file != null) {
                    System.out.println(this.getName() + ": " + "Parsing pdf " + file.getName());
                    //Parse pdf
                    parsePdf(file);
                    long _stop = System.currentTimeMillis();
                    long t = _stop - _start;
                    System.out.println("Processed pdf  " + file.getName() + " in " + t + " ms");
                }
            } catch (InterruptedException | ForcedStopException ex) {
                System.out.println("Something went wrong: " + ex.getMessage());
            }
        }
        if(!occurrences.isEmpty()) {
            //Update global Map
            try {
                monitor.updateGlobalOccurrences(occurrences, this.getName());
            } catch (ForcedStopException | InterruptedException ex) {
                System.out.println(this.getName() + ": Something went wrong " + ex);
            }
        }
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
                System.out.println("Processing word " + word);
                if(!exclusion.contains(word.toLowerCase())) {
                    //update occurrence
                    addOccurrences(word);
                    System.out.println(this.getName() + ": Processed actually " + numberOfRecordProcessed + " words");
                    System.out.println(this.getName() + ": The " + monitor.getN_occurrences() + " most frequent words actually for me are: " + getOccurrences(monitor.getN_occurrences()));
                } else {
                    System.out.println("Exclude word " + word.toLowerCase());
                }
            }
            document.close();
        } catch (IOException ex) {
            System.out.println("Something went wrong, please check " + ex.getMessage());
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

    public boolean running() {
       return monitor.getStop();
    }

    public Map<String, Integer> getOccurrences(int n) {
        if(occurrences.isEmpty()) {
            return null;
        }
        //Sorted map from value
        final Map<String, Integer> sortedByValue = sortByValue(occurrences);

        return sortedByValue.entrySet().stream()
                .limit(n)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private  static Map<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}

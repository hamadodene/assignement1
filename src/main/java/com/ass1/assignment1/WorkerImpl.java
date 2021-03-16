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
public class WorkerImpl extends Thread {

    private PDDocument document;
    private final Monitor monitor;
    private static final Logger LOG = Logger.getLogger(WorkerImpl.class.getName());
    private int numberOfRecordProcessed = 0;
    boolean verbose = Boolean.getBoolean("debug");

    public WorkerImpl(final String name, Monitor monitor) {
        super(name);
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (monitor.existNextFile()) {
            try {
                long _start = System.currentTimeMillis();
                File file = getFile();
                if (file != null) {
                    if(verbose) {
                        LOG.log(Level.FINE, "{0} Parsing pdf {1}", new Object[]{this.getName(), file.getName()});
                    }
                    System.out.println("Parsing pdf " + file.getName());
                    //Parse pdf
                    parsePdf(file);
                    long _stop = System.currentTimeMillis();
                    long t = _stop - _start;
                    System.out.println("Processed actually " + numberOfRecordProcessed + " words in "  + t + "ms");
                }
            } catch (InterruptedException | ForcedStopException ex) {
                this.interrupt();
            }
        }
    }

    private void parsePdf(File file) throws InterruptedException {
        try {
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfFIleInText = stripper.getText(document);
            String words[] = pdfFIleInText.split("\\r?\\n");
            List<String> exclusion = monitor.wordsToExclude();

            for (String word : words) {
                if(!exclusion.contains(word)) {
                    numberOfRecordProcessed = monitor.updateOccurrence(word);
                } else {
                    System.out.println("Exclude word " + word);
                }
            }
            document.close();
        } catch (IOException | ForcedStopException ex) {
            System.out.println("Something went wrong, please check " + ex);
        }
    }

    private File getFile() throws ForcedStopException, InterruptedException {
        return monitor.getNextFile();
    }

}

package com.ass1.assignment1;

import java.io.File;
import java.io.IOException;
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
    static final Logger LOG = Logger.getLogger(Worker.class.getName());
    private int numberOfRecordProcessed = 0;
    boolean verbose = Boolean.getBoolean("debug");

    public Worker(final String name, Monitor monitor) {
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
                    LOG.log(Level.INFO, "parsing pdf " + file.getName());
                    //Parse pdf
                    parsePdf(file);
                    long _stop = System.currentTimeMillis();
                    long t = _stop - _start;
                    if(verbose) {
                        LOG.log(Level.FINE, "Processed pdf {0} in {1} ms", new Object[]{file.getName(), t});
                        LOG.log(Level.FINE, "Processed actually {0} words", numberOfRecordProcessed);
                    }
                }
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, "{1} - Says: something went wrong {0}", new Object[]{this.getName(), ex});
            }
        }
        if(verbose) {
            LOG.log(Level.FINE, "{0} - Says: Nothing to do now, i go sleep", this.getName());
        }
    }

    void parsePdf(File file) throws InterruptedException {
        try {
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfFIleInText = stripper.getText(document);
            String words[] = pdfFIleInText.split("\\r?\\n");

            for (String word : words) {
                numberOfRecordProcessed = monitor.updateOccurence(word);
            }
            document.close();
        } catch (IOException | ForcedStopException ex) {
            LOG.log(Level.SEVERE, "{1} - Says: something went wrong {0}", new Object[]{this.getName(), ex});
        }
    }

    private File getFile() {
        return monitor.getNextFile();
    }
}

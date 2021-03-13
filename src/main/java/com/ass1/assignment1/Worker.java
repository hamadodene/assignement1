package com.ass1.assignment1;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author Hamado Dene
 */
public class Worker extends Thread {

    private PDDocument document;
    private final Monitor monitor;
    private final FilesProcessor dispatcher;
    static final Logger LOG = Logger.getLogger(Worker.class.getName());

    public Worker(Monitor monitor, FilesProcessor dispatcher) {
        this.monitor = monitor;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while (dispatcher.existNextFile()) {
            long _start = System.currentTimeMillis();
            File file = getFile();
            if (file != null) {
                try {
                    LOG.log(Level.INFO, "{0} Parsing pdf {1}", new Object[]{this.getName(), file.getName()});

                    //Parse pdf
                    parsePdf(file);
                    long _stop = System.currentTimeMillis();
                    long t = _stop - _start;

                    LOG.log(Level.INFO, "Processed pdf {0} in {1} ms", new Object[]{file.getName(), t});
                    LOG.log(Level.INFO, "Processed actualy {0} pdf", monitor.getNumberWordProcessed());
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, ex.toString());
                }
            }
        }
        LOG.log(Level.INFO, "Nothing to do now, i go sleep");
    }

    void parsePdf(File file) throws InterruptedException {
        try {
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfFIleInText = stripper.getText(document);
            String words[] = pdfFIleInText.split("\\r?\\n");

            for (String word : words) {
                monitor.updateOccurences(word);
                monitor.updataNumberWordProcessed();
            }
            document.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Somethiing went wrong {0}", e.toString());
        }
    }

    private File getFile() {
        return dispatcher.getNextFile();
    }
}

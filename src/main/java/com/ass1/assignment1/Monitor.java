package com.ass1.assignment1;

import java.io.File;
import java.util.logging.Logger;

/**
 *
 * @author Hamado Dene
 */
public class Monitor {

    static final Logger LOG = Logger.getLogger(Monitor.class.getName());
    private final Occurrences occurrences;
    private final FilesProcessor filesProcessor;
    

    public Monitor(Occurrences occurrences, FilesProcessor fileProcessor) {
        this.occurrences = occurrences;
        this.filesProcessor = fileProcessor;
    }

    //Update word occurrences
    public synchronized int updateOccurence(String word) {
        return occurrences.addOccurrence(word);
    }

    public synchronized File getNextFile() {
        return filesProcessor.getNextFile();
    }
    
    public boolean existNextFile() {
        return filesProcessor.existNextFile();
    }
}

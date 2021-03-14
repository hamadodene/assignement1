package com.ass1.assignment1;

import java.io.File;
import java.util.logging.Logger;

/**
 *
 * @author Hamado Dene
 */
public class Monitor {

    static final Logger LOG = Logger.getLogger(Monitor.class.getName());
    private final Occurences occurences;
    private final FilesProcessor filesProcessor;
    public int FORCE_STOP_VALUE = -99;
    

    public Monitor(Occurences occurences, FilesProcessor fileprocessor) {
        this.occurences = occurences;
        this.filesProcessor = fileprocessor;
    }

    //Update word occurences
    public synchronized int updateOccurences(String word) {    
        return occurences.addOccurences(word);
    }

    public synchronized File getNextFile() {
        return filesProcessor.getNextFile();
    }
    
    public boolean existNextFile() {
        return filesProcessor.existNextFile();
    }
}

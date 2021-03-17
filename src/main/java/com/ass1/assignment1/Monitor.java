package com.ass1.assignment1;

import com.ass1.assignment1.exception.ForcedStopException;
import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Hamado Dene
 */
public class Monitor {

    private final OccurrencesImpl occurrences;
    private final FilesProcessorImpl filesProcessor;
    private static Monitor _instance = null;
    private final String path;
    private final String file;
    private static int THREADS;
    private Lock mutex;

    public Monitor(OccurrencesImpl occurrences, String path, String file) {
        this.occurrences = occurrences;
        filesProcessor = new FilesProcessorImpl();
        mutex = new ReentrantLock();
        this.path = path;
        this.file = file;
    }

    public static Monitor _instance(OccurrencesImpl occurrences, String path, String file){
        if (_instance == null) {
            _instance = new Monitor(occurrences, path, file);
        }
        return _instance;
    }

    public void init() throws IncorrectDirectoryException, IncorrectFileException {
        if (Runtime.getRuntime().availableProcessors() < 3) {
            THREADS = 3;
        } else {
            THREADS = Runtime.getRuntime().availableProcessors();
        }
        initializePdfFiles(path);
        if(file == null) {
            System.out.println("Exclusion file does not specified, proceed without exclusions");
        } else {
            initializeExclusionWords(file);
        }
        if (THREADS > getNumberOfFiles()) {
            THREADS = getNumberOfFiles();
        }
    }

    //Update word occurrence
    public int updateOccurrence(String word) throws ForcedStopException, InterruptedException {
        mutex.lock();
        try {
            int result = occurrences.addOccurrence(word);
            return result;
        } finally {
            mutex.unlock();
        }
    }
    //Return the next file to parsing
    public File getNextFile() {
        mutex.lock();
        try {
            return filesProcessor.getNextFile();
        } finally {
            mutex.unlock();
        }
    }

    public boolean existNextFile() {
        return filesProcessor.existNextFile();
    }

    public List<String> wordsToExclude() {
        return filesProcessor.getWordsToExclude();
    }

    public int getWorkers() {
        return THREADS;
    }

    public int  getNumberOfFiles() {
        return filesProcessor.getFilesSize();
    }

    public void initializePdfFiles(String path) throws IncorrectDirectoryException {
        filesProcessor.initializePdfFiles(path);
    }

    public void initializeExclusionWords(String file) throws IncorrectFileException {
        filesProcessor.initializeWordsToExclude(file);
    }

    public void flushOccurrences() {
        occurrences.flushOccurrences();
    }

    public Map<String, Integer> getOccurrences(int n) {
        return occurrences.getOccurrences(n);
    }
}

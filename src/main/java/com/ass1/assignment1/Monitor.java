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
    private int n_occurrences;

    public Monitor(OccurrencesImpl occurrences, String path, String file) {
        this.occurrences = occurrences;
        filesProcessor = new FilesProcessorImpl();
        this.path = path;
        this.file = file;
    }

    public static Monitor _instance(OccurrencesImpl occurrences, String path, String file){
        if (_instance == null) {
            _instance = new Monitor(occurrences, path, file);
        }
        return _instance;
    }

    /**
     *
     * @throws IncorrectDirectoryException
     * @throws IncorrectFileException
     *
     * Initialize pdf files absolute path
     */
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

    /**
     *
     * @param word
     * @return number of words processed
     * @throws ForcedStopException
     * @throws InterruptedException
     *
     * Update word occurrence in the map
     */
    public synchronized void updateOccurrence(Map<String,Integer> words, String threadName){
        for (Map.Entry<String, Integer> word : words.entrySet()) {
            occurrences.addOccurrence(word.getKey(),word.getValue());
        }
        System.out.println(threadName + ": Update global Map");
    }

    /**
     *
     * @return the file to be assigned to the thread
     *
     */
    public synchronized File getNextFile() {
        return filesProcessor.getNextFile();
    }

    /**
     *
     * @return true if there is a least one file to process
     */
    public boolean existNextFile() {
        return filesProcessor.existNextFile();
    }

    /**
     *
     * @return list of words to exclude
     */
    public List<String> wordsToExclude() {
        return filesProcessor.getWordsToExclude();
    }

    //return number of threads
    public int getWorkers() {
        return THREADS;
    }

    //return number of pdf files
    public int  getNumberOfFiles() {
        return filesProcessor.getFilesSize();
    }

    /**
     *
     * @param path
     * @throws IncorrectDirectoryException
     *
     * Initialize pdf absolute path
     */
    public void initializePdfFiles(String path) throws IncorrectDirectoryException {
        filesProcessor.initializePdfFiles(path);
    }

    /**
     *
     * @param file
     * @throws IncorrectFileException
     *
     * Initialize words to exclude
     */
    public void initializeExclusionWords(String file) throws IncorrectFileException {
        filesProcessor.initializeWordsToExclude(file);
    }

    /**
     *
     * @param n
     * @return occurrences
     */
    public Map<String, Integer> getOccurrences(int n) {
        return occurrences.getOccurrences(n);
    }

    public int getTotalOfWordsProcessed() {
        return occurrences.getNumberWordsProcessed();
    }
    public void setN_occurrences(int n_occurrences) {
        this.n_occurrences = n_occurrences;
    }
}

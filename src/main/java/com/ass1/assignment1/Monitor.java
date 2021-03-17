package com.ass1.assignment1;

import com.ass1.assignment1.exception.ForcedStopException;
import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Hamado Dene
 * Monitor class
 */
public class Monitor {

    private final OccurrencesImpl occurrences;
    private final FilesProcessorImpl filesProcessor;
    private static Monitor _instance = null;
    private boolean started;
    private boolean forceStop;
    private static  int THREADS;

    public Monitor(OccurrencesImpl occurrences) {
        this.occurrences = occurrences;
        filesProcessor = new FilesProcessorImpl();
        this.started = false;
        this.forceStop = false;
    }

    public static Monitor _instance(OccurrencesImpl occurrences){
        if (_instance == null) {
            _instance = new Monitor(occurrences);
        }
        return _instance;
    }

    public void init() {
        if (Runtime.getRuntime().availableProcessors() < 3) {
            THREADS = 3;
        } else {
            THREADS = Runtime.getRuntime().availableProcessors();
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
    public synchronized int updateOccurrence(String word) throws ForcedStopException, InterruptedException {
        while (!started) {
            wait();
            if(forceStop) {
                throw new ForcedStopException("Force Stop thread");
            }
        }
        int result = occurrences.addOccurrence(word);
        notifyAll();
        return result;
    }

    /**
     *
     * @return the file to be assigned to the thread
     *
     */
    public synchronized File getNextFile() throws InterruptedException, ForcedStopException {
        while (!started) {
            wait();
            if(forceStop) {
                throw new ForcedStopException("Force Stop thread");
            }
        }
        notifyAll();
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

    /**
     *
     * @param forceStop
     * Force stop to all threads
     */
    public void forceStop(boolean forceStop) {
        this.forceStop = forceStop;
    }

    /**
     *
     * @param started
     * Set start
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

    /**
     *
     * @return number of threads
     */
    public int getWorkers() {
        return THREADS;
    }

    public boolean getStop() {
        return forceStop;
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
    /**
     *
     * @param n
     * @return occurrences
     */
    public Map<String, Integer> getOccurrences(int n) {
        return occurrences.getOccurrences(n);
    }

    public void resetIndex() {
        filesProcessor.resetNextFile();
    }
}

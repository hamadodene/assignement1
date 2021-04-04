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
    private boolean started;
    private boolean forceStop;
    private static  int THREADS;
    private int n_occurrences;
    private int totalOccurrences;
    private boolean debug = false;

    public Monitor(OccurrencesImpl occurrences) {
        this.occurrences = occurrences;
        filesProcessor = new FilesProcessorImpl();
        this.started = false;
        this.forceStop = false;
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
     * @throws ForcedStopException
     * @throws InterruptedException
     *
     * Update word occurrence in the map
     */
    public synchronized void updateGlobalOccurrences(Map<String, Integer> words , String threadName, int totalOccurrences) throws ForcedStopException, InterruptedException {
        while (!started) {
            wait();
            if(forceStop) {
                throw new ForcedStopException("Force Stop thread " + threadName);
            }
        }
        for (Map.Entry<String, Integer> word : words.entrySet()) {
            occurrences.addOccurrence(word.getKey(),word.getValue());
        }
        setTotalOccurrences(totalOccurrences);
        notifyAll();
    }

    /**
     *
     * @return the file to be assigned to the thread
     *
     */
    public synchronized File getNextFile(String threadName) throws InterruptedException, ForcedStopException {
        while (!started) {
            wait();
            if(forceStop) {
                throw new ForcedStopException("Force Stop thread " + threadName);
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

    /**
     *
     * @return occurrences
     */
    public Map<String, Integer> getOccurrences() {
        return occurrences.getOccurrences(n_occurrences);
    }

    public void flush() {
        filesProcessor.flush();
    }

    public void setN_occurrences(int n_occurrences) {
        this.n_occurrences = n_occurrences;
    }

    public void setTotalOccurrences(int n) {
        totalOccurrences = totalOccurrences + n;
    }

    public int getN_occurrences(){
        return this.n_occurrences;
    }
    public int getTotalOccurrences() {
        return this.totalOccurrences;
    }

    public boolean debug() {
        return debug;
    }
    public void enableDebug(boolean debug){
        this.debug = debug;
    }

}

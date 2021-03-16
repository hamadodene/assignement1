package com.ass1.assignment1;

import com.ass1.assignment1.exception.ForcedStopException;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Hamado Dene
 */
public class Monitor {

    static final Logger LOG = Logger.getLogger(Monitor.class.getName());
    private static Monitor _instance = null;
    private final OccurrencesImpl occurrences;
    private final FilesProcessorImpl filesProcessor;
    private boolean started;
    private boolean forceStop;
    public int FORCE_STOP_VALUE = -99;

    public Monitor(OccurrencesImpl occurrences, FilesProcessorImpl fileProcessor) {
        this.occurrences = occurrences;
        this.filesProcessor = fileProcessor;
        this.started = false;
        this.forceStop = false;
    }

    //Update word occurrence
    public synchronized int updateOccurrence(String word) throws ForcedStopException, InterruptedException {
        while (!started) {
            wait();
            if(forceStop) {
                throw new ForcedStopException();
            }
        }
        int result = occurrences.addOccurrence(word);
        notifyAll();
        return result;
    }

    public synchronized File getNextFile() throws InterruptedException, ForcedStopException {
        while (!started) {
            wait();
            if(forceStop) {
                throw new ForcedStopException();
            }
        }
        notifyAll();
        return filesProcessor.getNextFile();
    }

    public boolean existNextFile() {
        return filesProcessor.existNextFile();
    }

    public List<String> wordsToExclude() {
        return filesProcessor.getWordsToExclude();
    }

    public void forceStop(boolean forceStop) {
        this.forceStop = forceStop;
        notifyAll();
    }

    public synchronized void setStarted(boolean started) {
        this.started = started;
        notifyAll();
    }
}

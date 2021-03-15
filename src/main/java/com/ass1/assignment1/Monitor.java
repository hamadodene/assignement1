package com.ass1.assignment1;

import com.ass1.assignment1.exception.ForcedStopException;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author Hamado Dene
 */
public class Monitor {

    static final Logger LOG = Logger.getLogger(Monitor.class.getName());
    private final OccurrencesImpl occurrencesImpl;
    private final FilesProcessorImpl filesProcessorImpl;
    private boolean started;
    private boolean forceStop;
    public int FORCE_STOP_VALUE = -99;
    private Lock mutex;

    public Monitor(OccurrencesImpl occurrences, FilesProcessorImpl fileProcessor) {
        this.occurrencesImpl = occurrences;
        this.filesProcessorImpl = fileProcessor;
        mutex = new ReentrantLock();
        this.started = false;
        this.forceStop = false;
    }

    //Update word occurrence
    public int updateOccurrence(String word) throws ForcedStopException, InterruptedException {
        mutex.lock();
        try {
            int result = occurrencesImpl.addOccurrence(word);
            return result;
        } finally {
            mutex.unlock();
        }
    }

    public File getNextFile() {
        mutex.lock();
        try {
            return filesProcessorImpl.getNextFile();
        } finally {
            mutex.unlock();
        }
    }

    public boolean existNextFile() {
        return filesProcessorImpl.existNextFile();
    }

    public List<String> wordsToExclude() {
        return filesProcessorImpl.getWordsToExclude();
    }

    public void forceStop() {
        forceStop = true;
        notifyAll();
    }

    public synchronized  void setStarted(boolean started) {
        this.started = started;
        notifyAll();
    }
}

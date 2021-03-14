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
    private final Occurrences occurrences;
    private final FilesProcessor filesProcessor;
    private boolean forceStop;
    private Lock mutex;

    public Monitor(Occurrences occurrences, FilesProcessor fileProcessor) {
        this.occurrences = occurrences;
        this.filesProcessor = fileProcessor;
        mutex = new ReentrantLock();
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

    public void forceStop() {
        forceStop = true;
        notifyAll();
    }
}

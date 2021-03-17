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
    private final OccurrencesImpl occurrences;
    private final FilesProcessorImpl filesProcessor;
    private boolean started;
    private boolean forceStop;
    public int FORCE_STOP_VALUE = -99;
    private static  int THREADS;

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
        System.out.println("Call stop 1");
        this.forceStop = forceStop;
        //Maybe check if there are active threads
        notifyAll();
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getWorkers() {
        return THREADS;
    }

    public boolean getStop() {
        return forceStop;
    }

    public int  getNumberOfFiles() {
        return filesProcessor.getFilesSize();
    }

}

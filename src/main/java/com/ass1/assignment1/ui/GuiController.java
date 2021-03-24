package com.ass1.assignment1.ui;

import com.ass1.assignment1.Monitor;
import com.ass1.assignment1.OccurrencesImpl;
import com.ass1.assignment1.Worker;
import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamado Dene
 * Gui controller
 */
public class GuiController {

    private OccurrencesImpl occurrences;
    private Monitor monitor;
    private List<Worker> workers;

    public GuiController() {

    }

    /**
     * Initialize Gui
     */
    public void init() {
        new ShowListener().prepareGui(this);
    }

    public void setup(int n_occurrences) {
        occurrences = new OccurrencesImpl();
        monitor = new Monitor(occurrences);
        workers = new ArrayList<Worker>();
        monitor.setN_occurrences(n_occurrences);
        monitor.init();
    }

    /**
     * Start files parsing and log result
     */
    public void begin() {
        long _start = System.currentTimeMillis();
        int THREADS = monitor.getWorkers();

        if(THREADS > monitor.getNumberOfFiles()) {
            THREADS = monitor.getNumberOfFiles();
        }

        for(int i = 0; i <  THREADS; i++) {
            workers.add(new Worker("Worker-" + i, monitor));
        }
        System.out.println("Workers " + workers.toString());
        for (Worker worker : workers) {
            worker.start();
        }
        for (Worker worker: workers) {
            try {
                worker.join();
            }catch (InterruptedException ex) {
                System.out.println("Something went wrong: " + ex);
                worker.interrupt();
            }
        }
        long _stop = System.currentTimeMillis();
        long _result = _stop - _start;
        System.out.println("All work terminated in " + _result + " ms");
        monitor.flush();
    }

    /**
     * Set stop
     */
    public void stop() {
        monitor.forceStop(true);
    }

    /**
     * Set start
     */
    public void start() {
        monitor.setStarted(true);
    }

    /**
     *
     * @param path
     * @throws IncorrectDirectoryException
     * Initialize pdf files absolute path
     */
    public void initializePdfFiles(String path) throws IncorrectDirectoryException {
        monitor.initializePdfFiles(path);
    }

    /**
     *
     * @param file
     * @throws IncorrectFileException
     * Initialize words to exclude
     */
    public void initializeExclusionWords(String file) throws IncorrectFileException {
        monitor.initializeExclusionWords(file);
    }

    public  String printResult() {
        return monitor.getOccurrences().toString();
    }
}

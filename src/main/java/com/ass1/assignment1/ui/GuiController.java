package com.ass1.assignment1.ui;

import com.ass1.assignment1.Monitor;
import com.ass1.assignment1.OccurrencesImpl;
import com.ass1.assignment1.Worker;
import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.util.ArrayList;
import java.util.List;

public class GuiController {

    private OccurrencesImpl occurrences;
    private Monitor monitor;
    private List<Worker> workers;

    public GuiController() {
        occurrences = new OccurrencesImpl();
        monitor = Monitor._instance(occurrences);
        workers = new ArrayList<Worker>();
        monitor.init();
    }

    public void init() {
        new ShowListener().prepareGui(this);
    }

    public void begin() {
        long _start = System.currentTimeMillis();
        int THREADS = monitor.getWorkers();
        if(THREADS > monitor.getNumberOfFiles()) {
            THREADS = monitor.getNumberOfFiles();
        }
        for(int i = 0; i <  THREADS; i++) {
            workers.add(new Worker("Worker-" + i, monitor));
        }
        for (Worker worker : workers) {
            worker.run();
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
    }

    public void stop() {
        monitor.forceStop(true);
    }

    public void start() {
        monitor.setStarted(true);
    }

    public void initializePdfFiles(String path) throws IncorrectDirectoryException {
        monitor.initializePdfFiles(path);
    }

    public void initializeExclusionWords(String file) throws IncorrectFileException {
        monitor.initializeExclusionWords(file);
    }

}

package com.ass1.assignment1.ui;

import com.ass1.assignment1.Monitor;
import com.ass1.assignment1.OccurrencesImpl;
import com.ass1.assignment1.WorkerImpl;

import java.util.ArrayList;
import java.util.List;

public class GuiController {

    private OccurrencesImpl occurrences;
    private Monitor monitor;
    private List<WorkerImpl> workers;

    public GuiController() {
        occurrences = new OccurrencesImpl();
        monitor = new Monitor(occurrences);
        workers = new ArrayList<WorkerImpl>();
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
            workers.add(new WorkerImpl("Worker-" + i, monitor));
        }
        for (WorkerImpl worker : workers) {
            worker.run();
        }
        for (WorkerImpl worker: workers) {
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

}

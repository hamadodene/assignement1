package com.ass1.assignment1.ui;

import com.ass1.assignment1.FilesProcessorImpl;
import com.ass1.assignment1.Monitor;
import com.ass1.assignment1.OccurrencesImpl;
import com.ass1.assignment1.WorkerImp;
import com.ass1.assignment1.interfaces.Occurrences;

import java.util.ArrayList;
import java.util.List;

public class GuiController {

    private FilesProcessorImpl filesProcessor;
    private OccurrencesImpl occurrences;
    private Monitor monitor;

    public GuiController() {

    }

    public void init() {
        new ShowListener().prepareGui();
    }

    public void begin() {
        List<WorkerImp> workers = new ArrayList<WorkerImp>();

        for(int i = 0; i < filesProcessor.getWorkers(); i++) {
            workers.add(new WorkerImp("Worker-" + i, monitor));
        }

        long _start = System.currentTimeMillis();

        for (WorkerImp worker : workers) {
            worker.start();

        }
        for (WorkerImp worker: workers) {
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

package com.ass1.assignment1;

import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hamado Dene
 */
public class Main {
    static final Logger LOG = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws IncorrectDirectoryException, IncorrectFileException {
        System.out.println("Starting program");
        int n = 1;

        String path = "src/main/resources";
        String file = "src/main/resources/exclude.txt";

        int THREADS = 0;
        FilesProcessorImpl process = new FilesProcessorImpl(path, file);
        OccurrencesImpl occurrencesImpl = new OccurrencesImpl();
        Monitor monitor = new Monitor(occurrencesImpl,process);
        //Initialize
        process.init();
        List<WorkerImp> workers = new ArrayList<WorkerImp>();

        for(int i = 0; i <process.getWorkers(); i++) {
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
        System.out.println("All worker are terminated in " + _result + " ms");
        System.out.println("result is: " + occurrencesImpl.getOccurrences(n));
    }   
}

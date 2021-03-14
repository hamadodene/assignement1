package com.ass1.assignment1;

import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.util.ArrayList;
import java.util.List;
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
        String path = "src/main/resources";
        String file = "src/main/resources/exclude.txt";
        int n = 5;
        int THREADS = 0;
        FilesProcessor process = new FilesProcessor(path, file);
        Occurrences occurrences = new Occurrences();
        Monitor monitor = new Monitor(occurrences,process);
        //Initialize
        process.init();
        List<Worker> workers = new ArrayList<Worker>();

        for(int i = 0; i <process.getWorkers(); i++) {
            workers.add(new Worker("Worker-" + i, monitor));
        }

        long _start = System.currentTimeMillis();

        for (Worker worker : workers) {
            worker.start();
        }
        for (Worker worker: workers) {
            try {
                worker.join();
            }catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, "Something went wrong:  {0}", ex);
            }
        }

        long _stop = System.currentTimeMillis();
        long _result = _stop - _start;
        LOG.log(Level.INFO, "All worker are terminated in {0}  ms", _result );
        LOG.log(Level.INFO, "result is: {0}", occurrences.getOccurences(n));
    }   
}

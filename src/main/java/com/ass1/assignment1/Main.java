package com.ass1.assignment1;

/**
 *
 * @author Hamado Dene
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("Starting program");
        String path = "src/main/resources";
        int THREADS = 0;
        FilesProcessor process = new FilesProcessor(path);
        process.init();
        THREADS = process.getWorkers();
        System.out.println("Use " + THREADS  + " threads");
        Occurrences occurrences = new Occurrences();
        Monitor monitor = new Monitor(occurrences,process);
        Worker[] workers = new Worker[THREADS];
        for(int i  = 0 ; i < THREADS ; i++) {
            workers[i] = new Worker(monitor);
            workers[i].start();
        }
    }   
}

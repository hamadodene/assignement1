package com.ass1.assignment1;

/**
 *
 * @author Hamado Dene
 */
public class Main {
    
    public static void main(String[] args) {
        int THREADS = 0;
        System.out.println("Starting programm");
        String path = "src/main/resources";
        FilesProcessor process = new FilesProcessor(path);
        if(Runtime.getRuntime().availableProcessors() < 3){
            THREADS = 3;
        } else {
            THREADS = Runtime.getRuntime().availableProcessors();
        }       
        if(THREADS > process.getNumberOfFile()) {
            THREADS = process.getNumberOfFile();
        }
        System.out.println("Use " + THREADS  + " threads");
        Occurences occurences = new Occurences();
        Monitor monitor = new Monitor(occurences,process);
        Worker[] workers = new Worker[THREADS];
        for(int i  = 0 ; i < THREADS ; i++) {
            workers[i] = new Worker(monitor);
            workers[i].start();
        }
        
    }   
}

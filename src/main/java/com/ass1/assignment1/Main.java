package com.ass1.assignment1;

/**
 *
 * @author Hamado Dene
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("Starting programm");
        Monitor monitor = new Monitor();

        String path = "src/main/resources";
        FilesProcessor process = new FilesProcessor(path);

        Worker worker = new Worker(monitor, process);
        Worker worker2 = new Worker(monitor, process);
        Worker worker3 = new Worker(monitor, process);
        Worker worker4 = new Worker(monitor, process);
        worker.start();
        worker2.start();
        worker3.start();
        worker4.start();
    }
}

package com.ass1.assignment1;

import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Hamado Dene
 */
public class Main {
    static final Logger LOG = Logger.getLogger("Find the most frequent words in a group of files");
    private static boolean pathOk = false;
    private static boolean n_occurrencesOk = false;
    private static boolean fileOk = false;
    private static String path;
    private static String file;
    private static int n;


    public static void main(String[] args) throws IncorrectDirectoryException, IncorrectFileException {

        System.out.println("Starting program");
        /**
         * Get pdf path from command line
         * Retry if input is not a directory
         */
        do {
            Scanner scanner = new Scanner(System.in);
            LOG.log(Level.INFO, "Enter pdf files path");
            try {
                path = scanner.nextLine();
                System.out.println(path);
                if (path.isEmpty() || !new File(path).isDirectory()) {
                    throw new Exception();
                }
                pathOk = true;
            } catch (Exception e) {
                System.out.println("Please insert a correct path");
                scanner.reset();
            }

        } while (!pathOk);

        /**
         * Get file that contain the words to exclude
         * If the input is empty, the program continue without exclusion
         */
        Scanner scanner = new Scanner(System.in);
        LOG.log(Level.INFO, "Enter exclusion files path");
        if (!scanner.hasNextLine()) {
            System.out.println("You have not indicated exclusion file, we will proceed without any exclusion");
            file = null;
        } else {
            do {
                try {
                    file = scanner.nextLine();
                    if (!new File(file).isFile()) {
                        throw new Exception();
                    }
                    fileOk = true;
                } catch (Exception e) {
                    System.out.println("Please insert a valid file");
                    scanner.reset();
                }
            } while (!fileOk);
        }

        /**
         * Get the numbers of occurrences that the user want to print
         * Retry if input not an integer
         */
        do {
            Scanner scanner_n = new Scanner(System.in);
            try {
                LOG.log(Level.INFO, "Enter number of occurrences you want print");
                if (scanner_n.hasNextInt()) {
                    n = scanner_n.nextInt();
                    n_occurrencesOk = true;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Please insert a valid number");
                scanner_n.nextLine();
            }
        } while (!n_occurrencesOk);

        /**
         * Initialize monitor
         */
        Monitor monitor = Monitor._instance(new OccurrencesImpl(), path, file);
        monitor.setN_occurrences(n);
        /**
         * Init
         */
        monitor.init();

        /**
         * List of workers
         */
        List<Worker> workers = new ArrayList<Worker>();

        /**
         * Populate list based on number of threads
         */
        for (int i = 0; i < monitor.getWorkers(); i++) {
            workers.add(new Worker("Worker-" + i, monitor));
        }

        long _start = System.currentTimeMillis();

        /**
         * Start worker
         */
        for (Worker worker : workers) {
            worker.start();
        }

        /**
         *  Join:  To ensure to print result only  if all threads have finished
         */
        for (Worker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, "Something went wrong:  {0}", ex);
                worker.interrupt();
            }
        }

        long _stop = System.currentTimeMillis();
        long _result = _stop - _start;
        LOG.log(Level.INFO, "All worker are terminated in {0}  ms", _result);
        /**
         *  Print final results
         */
        LOG.log(Level.INFO, "result is: {0}", monitor.getOccurrences(n));
        LOG.log(Level.INFO, "The total number of words processed are: " + monitor.getTotalOfWordsProcessed());
    }
}

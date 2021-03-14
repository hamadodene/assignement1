package com.ass1.assignment1;

import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Hamado Dene
 */
public final class FilesProcessor {

    static final Logger LOG = Logger.getLogger(FilesProcessor.class.getName());
    private static final ArrayList<String> pdfFilesAbsolutePath = new ArrayList<String>();
    private static List<String> wordsToExclude = new ArrayList<String>();
    private final String directory;
    private final String file;
    private int numberOfFile = 0;
    private static int nextFile = -1;
    private static int THREADS;

    public FilesProcessor(String directory, String file) {
        this.directory = directory;
        this.file = file;
    }

    public void init() throws IncorrectDirectoryException, IncorrectFileException {
        if (Runtime.getRuntime().availableProcessors() < 3) {
            THREADS = 3;
        } else {
            THREADS = Runtime.getRuntime().availableProcessors();
        }
        initializePdfFiles(new File(directory));
        if(file == null) {
            LOG.log(Level.INFO, "Exclusion file does not specified, Proceed without exclusions");
        } else {
            initializeWordsToExclude(new File(file));
        }
        if (THREADS > pdfFilesAbsolutePath.size()) {
            THREADS = pdfFilesAbsolutePath.size();
        }
    }

    private void initializePdfFiles(final File folder) throws IncorrectDirectoryException {
        String tempFileName = "";
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IncorrectDirectoryException("Wrong folder pass, please correct");
        }
        for (final File entry : folder.listFiles()) {
            if (entry.isFile()) {
                String extension = getExtensionByStringHandling(entry.getName());
                if (!"pdf".equals(extension)) {
                    LOG.log(Level.INFO, "Skipping file {0} because not a pdf files", entry.getName());
                } else {
                    tempFileName = entry.getAbsolutePath();
                    pdfFilesAbsolutePath.add(tempFileName);
                }
            } else {
                LOG.log(Level.INFO, "Skipping directory {0}", entry.getAbsolutePath());
            }
        }

        numberOfFile = pdfFilesAbsolutePath.size();
    }

    private void initializeWordsToExclude (final File file) throws IncorrectFileException {
        if(!file.isFile() || !file.exists()) {
            throw new IncorrectFileException("File not correct, please check");
        }
        boolean format = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))){
            String line = br.readLine();
            while (line != null) {
                if(line.matches(".*([ \\t]).*")){
                    throw new IncorrectFileException("File format not correct, please check");
                } else {
                    wordsToExclude.add(line);
                    line = br.readLine();
                }
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Something went wrong, please check {0}", e);
        }
    }

    public File getNextFile() {
        nextFile++;
        if (nextFile < pdfFilesAbsolutePath.size()) {
            return new File(pdfFilesAbsolutePath.get(nextFile));
        } else {
            return null;
        }
    }

    private String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse("");
    }

    public int getNumberOfFile() {
        return this.numberOfFile;
    }

    public boolean existNextFile() {
        if (nextFile < pdfFilesAbsolutePath.size()) {
            return true;
        }
        return false;
    }

    public int getWorkers() {
        return THREADS;
    }
    public List<String> getWordsToExclude() {
        return wordsToExclude;
    }
}

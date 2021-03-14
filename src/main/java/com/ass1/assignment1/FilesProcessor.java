package com.ass1.assignment1;

import com.ass1.assignment1.exception.IncorrectDirectoryException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Hamado Dene
 */
public final class FilesProcessor {

    static final Logger LOG = Logger.getLogger(FilesProcessor.class.getName());
    private static final ArrayList<String> pdfFilesAbsolutePath = new ArrayList<String>();
    private final String directory;
    private int numberOfFile = 0;
    private static int nextFile = -1;
    private static int THREADS;

    public FilesProcessor(String directory) {
        this.directory = directory;
    }

    public void init() throws IncorrectDirectoryException {
        if (Runtime.getRuntime().availableProcessors() < 3) {
            THREADS = 3;
        } else {
            THREADS = Runtime.getRuntime().availableProcessors();
        }
        initializePdfFiles(new File(directory));

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
}

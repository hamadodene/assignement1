package com.ass1.assignment1;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hamado Dene
 */
public final class FilesProcessor {

    static final Logger LOG = Logger.getLogger(FilesProcessor.class.getName());
    private static final ArrayList<String> allFiles = new ArrayList<String>();
    private final String directory;
    private int numberOfFile = 0;
    private static int nextFile = -1;

    public FilesProcessor(String directory) {
        this.directory = directory;
        getAllFilesInArrayList(new File(directory));
    }

    private void getAllFilesInArrayList(final File folder) {
        String tempFileName = "";
        if (folder.isDirectory()) {
            for (final File entry : folder.listFiles()) {
                if (entry.isFile()) {
                    String extension = getExtensionByStringHandling(entry.getName());
                    if (!"pdf".equals(extension)) {
                        LOG.log(Level.INFO, "Skipping file {0} because not a pdf files", entry.getName());
                    } else {
                        tempFileName = entry.getAbsolutePath();
                        allFiles.add(tempFileName);
                    }
                } else {
                    LOG.log(Level.INFO, "Skiping direcory {0}", entry.getAbsolutePath());
                }
            }
        }
        if (folder.isFile()) {
            tempFileName = folder.getAbsolutePath();
            allFiles.add(tempFileName);
        }
        numberOfFile = allFiles.size();
    }

    public synchronized File getNextFile() {
        nextFile++;
        if (nextFile < allFiles.size()) {
            return new File(allFiles.get(nextFile));
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

    public ArrayList<String> files() {
        return allFiles;
    }

    public boolean existNextFile() {
        if (nextFile < allFiles.size()) {
            return true;
        }
        return false;
    }
}

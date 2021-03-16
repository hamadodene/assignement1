package com.ass1.assignment1;

import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;
import com.ass1.assignment1.interfaces.FileProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author Hamado Dene
 */
public final class FilesProcessorImpl implements FileProcessor {

    static final Logger LOG = Logger.getLogger(FilesProcessorImpl.class.getName());
    private static final ArrayList<String> pdfFilesAbsolutePath = new ArrayList<String>();
    private static List<String> wordsToExclude = new ArrayList<String>();
    private final String directory;
    private final String file;
    private int numberOfFile = 0;
    private static int nextFile = -1;
    private static int THREADS;

    public FilesProcessorImpl(String directory, String file) {
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
            System.out.println("Exclusion file does not specified, Proceed without exclusions");
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
                    System.out.println("Skipping file" + entry.getName() +  " because not a pdf files");
                } else {
                    tempFileName = entry.getAbsolutePath();
                    pdfFilesAbsolutePath.add(tempFileName);
                }
            } else {
                System.out.println("Skipping directory " + entry.getAbsolutePath());
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
                    throw new IncorrectFileException("File " + file.getName() + "format not correct, please check");
                } else {
                    wordsToExclude.add(line);
                    line = br.readLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong, please check " + e );
        }
    }

    @Override
    public File getNextFile() {
        nextFile++;
        if (nextFile < pdfFilesAbsolutePath.size()) {
            return new File(pdfFilesAbsolutePath.get(nextFile));
        } else {
            return null;
        }
    }

    @Override
    public String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse("");
    }

    @Override
    public int getNumberOfFile() {
        return this.numberOfFile;
    }

    @Override
    public boolean existNextFile() {
        if (nextFile < pdfFilesAbsolutePath.size()) {
            return true;
        }
        return false;
    }
    @Override
    public int getWorkers() {
        return THREADS;
    }

    @Override
    public List<String> getWordsToExclude() {
        return wordsToExclude;
    }
}

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
 * Files management class
 */
public class FilesProcessorImpl implements FileProcessor {

    static final Logger LOG = Logger.getLogger(FilesProcessorImpl.class.getName());
    private static final ArrayList<String> pdfFilesAbsolutePath = new ArrayList<String>();
    private static List<String> wordsToExclude = new ArrayList<String>();
    private int numberOfFile = 0;
    private static int nextFile = -1;
    private static int THREADS;

    public FilesProcessorImpl() {
    }

    public void initializePdfFiles(final String path) throws IncorrectDirectoryException {
        File folder = new File(path);

        String tempFileName = "";
        if (!folder.exists() || !folder.isDirectory() || folder == null) {
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

    public void initializeWordsToExclude (final String exclusionFile) throws IncorrectFileException {
        File file = new File(exclusionFile);

        if(!file.isFile() || !file.exists() || file == null) {
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

    public int getFilesSize() {
        return pdfFilesAbsolutePath.size();
    }

    public void resetNextFile() {
        if(nextFile != -1) {
            nextFile = -1;
        }
    }

    public void flush(){
        if(pdfFilesAbsolutePath.size() > 0 ) {
            pdfFilesAbsolutePath.clear();
        }
        if(wordsToExclude.size() > 0 ) {
            wordsToExclude.clear();
        }
        nextFile = -1;
    }
}

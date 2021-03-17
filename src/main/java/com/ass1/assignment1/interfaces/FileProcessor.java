package com.ass1.assignment1.interfaces;

import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.io.File;
import java.util.List;

public interface FileProcessor {
    public int getFilesSize();
    public File getNextFile();
    public boolean existNextFile();
    public List<String> getWordsToExclude();
    public String getExtensionByStringHandling(String fileName);
    public void initializePdfFiles(final String path) throws IncorrectDirectoryException;
    public void initializeWordsToExclude (final String exclusionFile) throws IncorrectFileException;
}

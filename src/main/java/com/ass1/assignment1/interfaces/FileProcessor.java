package com.ass1.assignment1.interfaces;

import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import java.io.File;
import java.util.List;

public interface FileProcessor {
    public File getNextFile();
    public int getNumberOfFile();
    public boolean existNextFile();
    public int getWorkers();
    public List<String> getWordsToExclude();
    public String getExtensionByStringHandling(String fileName);
}

package com.ass1.assignment1;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Hamado Dene
 */
public class InitializePdfFiles extends Thread{
    
    private static final ArrayList<String> allFiles = new ArrayList<String>();
    private final String directory;
    private int numberOfFile = 0;
    private static int nextFile = -1;
    
    public InitializePdfFiles( String directory) {
        this.directory = directory;
    }
    
    @Override
    public void run(){
        File folder = new File(directory);
        getAllFilesInArrayList(folder);
    }
    
    public void getAllFilesInArrayList(final File folder) {
        String tempFileName = "";
        if(folder.isDirectory()){
            for (final File entry: folder.listFiles()) {
                if(entry.isFile()) {         
                    String extension = getExtensionByStringHandling(entry.getName());
                    if( !"pdf".equals(extension)){
                        System.out.println("Skipping file " + entry.getName() + " because not a pdf files");
                    } else {
                        tempFileName = entry.getAbsolutePath();
                        allFiles.add(tempFileName);
                    }
                } else {
                    System.out.println("Skiping direcory " + entry.getAbsolutePath());
                }
            }
        }         
        if(folder.isFile()) {
            tempFileName = folder.getAbsolutePath();
            allFiles.add(tempFileName);
        }
        numberOfFile = allFiles.size();        
    }
    
    public synchronized File getNextFile() {
        nextFile++;
        if(nextFile < allFiles.size()){
            return new File(allFiles.get(nextFile));
        } else {
            System.out.println("Ops, nothing to do now");
            return null;
        }
    }
    
    public String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
          .filter(f -> f.contains("."))
          .map(f -> f.substring(filename.lastIndexOf(".") + 1))
          .orElse("");
    }
    
    public int getNumberOfFile() {
        return this.numberOfFile;
    }
}

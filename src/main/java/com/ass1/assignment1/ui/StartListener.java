package com.ass1.assignment1.ui;

import com.ass1.assignment1.FilesProcessorImpl;
import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;
import org.apache.commons.logging.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartListener implements ActionListener {

    private FilesProcessorImpl filesProcessor;
    private JTextField field1;
    private JTextField field2;
    private JTextField field3;
    private GuiController controller;
    private boolean started = false;

    public StartListener(JTextField path, JTextField file, JTextField n, GuiController controller) {
        this.field1 = path;
        this.field2 = file;
        this.field3 = n;
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String path = field1.getText().trim();
        String file = field2.getText().trim();
        int n = Integer.parseInt(field3.getText().trim());
        try {
            new FilesProcessorImpl().initializePdfFiles(path);
            new FilesProcessorImpl().initializeWordsToExclude(file);
        } catch (IncorrectDirectoryException incorrectDirectoryException) {
            System.out.println("Directory " + path + " not correct, please check");
        } catch (IncorrectFileException incorrectFileException) {
            System.out.println("File " + file + " not correct, please check");
        }

        //set started = true
        controller.start();
        //Send work
        controller.begin();
    }
}

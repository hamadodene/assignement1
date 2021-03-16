package com.ass1.assignment1.ui;

import com.ass1.assignment1.FilesProcessorImpl;
import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartListener implements ActionListener {

    FilesProcessorImpl filesProcessor;
    JTextField field1;
    JTextField field2;
    JTextField field3;

    public StartListener(JTextField path, JTextField file, JTextField n) {
        this.field1 = path;
        this.field2 = file;
        this.field3 = n;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String path = field1.getText().trim();
        String file = field2.getText().trim();
        int n = Integer.parseInt(field3.getText().trim());

        filesProcessor = new FilesProcessorImpl(path,file);
        try {
            filesProcessor.init();
        } catch (IncorrectDirectoryException incorrectDirectoryException) {
            System.out.println("Directory " + path + " not correct, please check");
        } catch (IncorrectFileException incorrectFileException) {
            System.out.println("File " + file + " not correct, please check");
        }
    }
}

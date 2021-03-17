package com.ass1.assignment1.ui;

import com.ass1.assignment1.FilesProcessorImpl;
import com.ass1.assignment1.exception.IncorrectDirectoryException;
import com.ass1.assignment1.exception.IncorrectFileException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Hamado Dene
 * Start button listener
 */
public class StartListener implements ActionListener {

    private JTextField field1;
    private JTextField field2;
    private JTextField field3;
    private JLabel result;
    private GuiController controller;
    private boolean started = false;
    private String path;
    private String file;
    private int n;

    public StartListener(JTextField path, JTextField file, JTextField n, JLabel result, GuiController controller) {
        this.field1 = path;
        this.field2 = file;
        this.field3 = n;
        this.result = result;
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        path = field1.getText().trim();
        file = field2.getText().trim();
        if (path.isEmpty() || !new File(path).isDirectory()) {
            JOptionPane.showMessageDialog(null, "Please enter a correct path");
            return;
        }
        if (file.isEmpty()) {
            file = null;
        } else if (!new File(file).isFile()) {
            JOptionPane.showMessageDialog(null, "Please enter a correct file");
            return;
        }

        String occurrences = field3.getText().trim();
        try {
            n = Integer.parseInt(occurrences);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a correct number");
            return;
        }

        new Thread(() -> {
            try {
                //Initialize pdf files
                controller.initializePdfFiles(path);
                if (file == null) {
                    System.out.println("You have not specified an exclusion file, we will proceed without it");
                } else {
                    //Initialize exclusion words
                    controller.initializeExclusionWords(file);
                }
            } catch (IncorrectDirectoryException | IncorrectFileException ex) {
                System.out.println("Detected some error: " + ex.getMessage());
            }
            //set started = true
            controller.start();
            //Send work
            controller.begin();
            result.setText(controller.printResult(n));
        }).start();
    }

    private static void validatePath() {

    }

}

package com.ass1.assignment1.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Hamado Dene
 * Stop button listener
 */
public class StopListener implements ActionListener {

    private GuiController controller;

    public StopListener(GuiController controller) {
        this.controller = controller;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //Send stop to worker
        controller.stop();
    }
}

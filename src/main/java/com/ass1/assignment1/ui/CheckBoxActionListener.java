package com.ass1.assignment1.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckBoxActionListener implements ActionListener {
    private JCheckBox checkBox;
    private GuiController controller;

    public CheckBoxActionListener(JCheckBox checkBox, GuiController controller) {
        this.checkBox = checkBox;
        this.controller = controller;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(checkBox.isSelected()){
            controller.setDebug(true);
        }
    }
}

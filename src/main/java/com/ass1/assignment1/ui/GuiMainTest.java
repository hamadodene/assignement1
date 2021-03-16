package com.ass1.assignment1.ui;

import com.ass1.assignment1.WorkerImpl;

import java.util.logging.Logger;

public class GuiMainTest {
    private static final Logger LOG = Logger.getLogger(WorkerImpl.class.getName());
    public static void main(String[] args) {
        new GuiController().init();
    }
}

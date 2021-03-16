package com.ass1.assignment1.ui;

import com.ass1.assignment1.FilesProcessorImpl;
import com.ass1.assignment1.Monitor;
import com.ass1.assignment1.logger.CustomPrintStream;
import com.ass1.assignment1.logger.GuiOutPutStream;

import javax.swing.*;
import java.io.PrintStream;

public class ShowListener {
    private JFrame mainFrame;
    private JTextField field1,field2,field3;
    private GuiOutPutStream output;

    public void prepareGui() {
        mainFrame = new JFrame("PCD Assignment 1 - GUI version");
        mainFrame.setBounds(100, 100, 730, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(null);

        field1 = new JTextField();
        field1.setBounds(180, 28, 200, 20);
        mainFrame.getContentPane().add(field1);
        field1.setColumns(10);

        JLabel fl1 = new JLabel("Pdf path");
        fl1.setBounds(65, 31, 50, 14);
        mainFrame.getContentPane().add(fl1);

        JLabel fl2 = new JLabel("Exclusion file");
        fl2.setBounds(65, 68, 100, 14);
        mainFrame.getContentPane().add(fl2);

        field2 = new JTextField();
        field2.setBounds(180, 65, 100, 20);
        mainFrame.getContentPane().add(field2);
        field2.setColumns(10);

        JLabel flp3 = new JLabel("N°Occurrences");
        flp3.setBounds(65, 115, 100, 14);
        mainFrame.getContentPane().add(flp3);

        field3 = new JTextField();
        field3.setBounds(180, 112, 247, 17);
        mainFrame.getContentPane().add(field3);
        field3.setColumns(10);

        JButton start = new JButton("Start");

        start.setBounds(312, 180, 89, 23);
        mainFrame.getContentPane().add(start);

        JButton stop = new JButton("Stop");
        stop.setBounds(65, 180, 89, 23);
        mainFrame.getContentPane().add(stop);

        JTextArea log = new JTextArea(30,30);
        JScrollPane scroll = new JScrollPane(log);
        scroll.setBounds(50, 250, 500, 250);
        log.setEditable(false);

        output = new GuiOutPutStream(log);
        System.setOut(new PrintStream(output, true));
        System.setOut(new CustomPrintStream(System.out));

        mainFrame.getContentPane().add(scroll);
        
        start.addActionListener(new StartListener(field1, field2, field3));
        stop.addActionListener(new StopListener());

        mainFrame.setVisible(true);
    }

}

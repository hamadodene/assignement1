package com.ass1.assignment1.logger;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class GuiOutPutStream extends OutputStream
{
    JTextArea textArea;

    public GuiOutPutStream(JTextArea textArea){
        this.textArea = textArea;
    }

    @Override
    public void write(int data) throws IOException {
        textArea.append(new String(new byte[] {(byte)data }));
    }
}

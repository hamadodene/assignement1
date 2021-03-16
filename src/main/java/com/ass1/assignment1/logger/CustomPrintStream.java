package com.ass1.assignment1.logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomPrintStream extends PrintStream {

    public CustomPrintStream(OutputStream out) {
        super(out);
    }

    @Override
    public void println(String string) {
        String pattern = "yyyy-MM-dd-hh-mm-ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format( new Date());
        super.println("[" + date + "] " + string);
    }
}

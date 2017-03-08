/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Log;


import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private final static String FILE_NAME = "MultimediaTestServer.log";
    private static PrintWriter sWriter;

    public static void init(String filePath) {
        try {
            File file = new File(filePath + FILE_NAME);
            System.out.println(file.getAbsolutePath());
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Cannot create log file");
                }
            }

            sWriter = new PrintWriter(new FileOutputStream(file, true));
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            PrintWriter print = new PrintWriter(writer);
            e.printStackTrace(print);
            System.out.println(writer.toString());
            System.out.println(e.getMessage());
        }
    }

    public static void writeLogEntry(String from, String msg) {
        if (sWriter != null) {
            sWriter.println(getDate() + " - " + from + ": " + msg);
        }
    }

    public static void writeException(String from, Exception e) {
        if (sWriter != null) {
            sWriter.print(getDate() + " - " + from + ": ");
            e.printStackTrace(sWriter);
            sWriter.flush();
        }
    }

    public static void close() {
        if (sWriter != null) {
            sWriter.close();
        }
    }

    private static String getDate() {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return format.format(date);
    }
}

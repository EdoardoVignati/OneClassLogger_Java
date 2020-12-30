package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

/**
 * Author @edoardovignati
 * Code available at https://github.com/EdoardoVignati/OneClassLogger_Java
 */

public class OneClassLogger {
    private Class clazz;
    private ArrayList<OneClassLoggerOption> options;
    private static ArrayList<LEVEL> levels;
    private String optionsPath;

    // Add here a new custom appender
    private enum APPENDER {
        _CONSOLE,
        _FILE
    }

    // Add here a new custom level
    private enum LEVEL {
        DEBUG,
        WARN,
    }

    // With or without options file from argument -Dlog
    public OneClassLogger(Class clazz) {
        this.clazz = clazz;
        this.levels = new ArrayList<>(Arrays.asList(LEVEL.values()));
        this.optionsPath = System.getProperty("log");
        this.options = load();
    }

    // With options file
    public OneClassLogger(Class clazz, String optionsFilePath) {
        this.clazz = clazz;
        this.optionsPath = optionsFilePath;
        this.levels = new ArrayList<>(Arrays.asList(LEVEL.values()));
        this.options = load();
    }

    public void debug(String message) {
        log(LEVEL.DEBUG, message);
    }

    public void warn(String message) {
        log(LEVEL.WARN, message);
    }

    private void log(LEVEL level, String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int lineNumber = stackTrace[3].getLineNumber();
        boolean logged = false;

        String out = now() + " [" + level + "] " + clazz.getName() + ":" + lineNumber + " - " + message;

        for (OneClassLoggerOption t : options) {
            if (clazz.getName().matches(t.getMatcher()) && t.getLevel().equals(level.toString())) {
                if (t.getAppender().equals(APPENDER._CONSOLE.toString())) {
                    System.out.println(out);
                    logged = true;
                }
                if (t.getAppender().split(":")[0].equals(APPENDER._FILE.toString())) {
                    File f = new File(t.getAppender().split(":")[1]);

                    try {
                        if (!f.exists() || f.isDirectory()) {
                            f.createNewFile();
                        }

                        BufferedWriter writer = new BufferedWriter(
                                new FileWriter(f.getAbsolutePath(), true));

                        writer.write(out + "\n");
                        writer.close();
                        logged = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        // Select console by default
        if (!logged) {
            System.out.println(out);
            return;
        }
    }

    private static String now() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    private ArrayList<OneClassLoggerOption> load() {
        ArrayList<OneClassLoggerOption> opts = new ArrayList<>();
        if (optionsPath != null) {
            try {
                opts = new ArrayList<>();
                File file = new File(optionsPath);
                if (!file.exists() || file.isDirectory())
                    throw new Exception("Log file error");
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (data.trim().length() == 0)
                        continue;
                    String[] option = data.split("\\s+");
                    if (option.length != 3)
                        throw new Exception("Element missing in log option");
                    for (String l : option[1].split(",")) {
                        if (levels.contains(LEVEL.valueOf(l.toUpperCase())))
                            opts.add(new OneClassLoggerOption(option[0], l.toUpperCase(), option[2]));
                        else throw new Exception("Log level not valid: " + l);
                    }
                }
                myReader.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return opts;
    }
}

class OneClassLoggerOption {
    String matcher;
    String level;
    String appender;

    OneClassLoggerOption(String m, String l, String a) {
        matcher = m;
        level = l;
        appender = a;
    }

    public String getLevel() {
        return level;
    }

    public String getMatcher() {
        return matcher;
    }

    public String getAppender() {
        return appender;
    }
}

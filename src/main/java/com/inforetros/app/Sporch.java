package com.inforetros.app;

import com.inforetros.command.SporchCommand;
import picocli.CommandLine;
public class Sporch {

    public static final String INDEX_PATH = "kaggle.com\\index";
    public static final String R_INDEX_PATH = "kaggle.com\\r_index";
    public static final String JSON_FOLDER = "kaggle.com\\data\\subset";
    public static void main(String[] args) {
        int exitCode = new CommandLine(new SporchCommand()).execute(args);
        System.exit(exitCode);
    }

}

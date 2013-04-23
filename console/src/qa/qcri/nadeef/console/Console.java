/*
 * Copyright (C) Qatar Computing Research Institute, 2013.
 * All rights reserved.
 */

package qa.qcri.nadeef.console;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jline.Terminal;
import jline.TerminalFactory;
import jline.console.ConsoleReader;
import jline.console.completer.*;
import qa.qcri.nadeef.core.datamodel.CleanPlan;
import qa.qcri.nadeef.core.datamodel.Rule;
import qa.qcri.nadeef.core.pipeline.CleanExecutor;
import qa.qcri.nadeef.core.util.Bootstrap;

/**
 * User interactive console.
 */
public class Console {

    //<editor-fold desc="Private fields">
    private static final String logo =
            "   _  __        __        _____\n" +
            "  / |/ /__ ____/ /__ ___ /   _/\n" +
            " /    / _ `/ _  / -_) -_)   _/\n" +
            "/_/|_/\\_,_/\\_,_/\\__/\\__/ __/\n" +
            "Data Cleaning solution (Build " + System.getenv("BuildVersion")  +
            ", using Java " + System.getProperty("java.version") + ").\n" +
            "Copyright (C) Qatar Computing Research Institute, 2013 (http://da.qcri.org).";

    private static final String helpInfo = "Type 'help' to see what commands we have.";

    private static final String prompt = ":> ";

    private static final String[] commands = { "load", "run", "repair", "help", "set", "exit" };
    private static ConsoleReader console;
    private static CleanPlan currentCleanPlan;
    private static CleanExecutor executor;

    //</editor-fold>

    /**
     * Start of Console.
     * @param args user input.
     */
    public static void main(String[] args) {
        try {
            // bootstrap Nadeef.
            Bootstrap.Start();
            console = new ConsoleReader();
            console.println(logo);
            console.println();
            console.println(helpInfo);
            console.println();
            console.drawLine();
            String line;

            console.setPrompt(prompt);

            List<Completer> loadCompleter =
                    Arrays.asList(
                        new StringsCompleter(commands),
                        new FileNameCompleter(),
                        new NullCompleter()
                    );
            console.addCompleter(new ArgumentCompleter(loadCompleter));

            while ((line = console.readLine()) != null) {
                try {
                    if (line.equalsIgnoreCase("exit")) {
                        break;
                    } else if (line.startsWith("load")) {
                        load(line);
                    } else if (line.startsWith("list")) {
                        list();
                    } else if (line.startsWith("help")) {
                        printHelp();
                    } else if (line.startsWith("detect")) {
                        detect(line);
                    } else {
                        console.println("I don't know this command.");
                    }
                } catch (Exception ex) {
                    console.println("Oops, exceptions happens:");
                    console.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            try {
                console.println("Bootstrap failed.");
                ex.printStackTrace();
            } catch (Exception ignore) {}
        }

        System.exit(0);
    }

    private static void load(String cmdLine) throws IOException {
        String[] splits = cmdLine.split("\\s");
        if (splits.length != 2) {
            throw new IllegalArgumentException(
                "Invalid load command. Run load <Nadeef config file>."
            );
        }
        String fileName = splits[1];
        if (Bootstrap.isWindows()) {
            fileName = fileName.replace('/', '\\');
        }
        CleanPlan plan;
        try {
            currentCleanPlan = CleanPlan.createCleanPlanFromJSON(new FileReader(fileName));
        } catch (Exception ex) {
            console.println(ex.getMessage());
            return;
        }

        executor = new CleanExecutor(currentCleanPlan);
        console.println(currentCleanPlan.getRules().size() + " rules loaded.");
    }

    private static void list() throws IOException {
        List<Rule> rules = currentCleanPlan.getRules();
        for (int i = 0; i < rules.size(); i ++) {
            console.println("\t" + i + ": " + rules.get(i).getId());
        }
    }

    private static void detect(String cmd) throws IOException {
        String[] splits = cmd.split("\\s");
        if (splits.length > 2) {
            throw
                new IllegalArgumentException(
                    "Invalid detect command. Run detect [id number] instead."
                );
        }
        executor.run();
    }

    private static void printHelp() throws IOException {
        String help =
                " |Nadeef console usage:\n" +
                " |----------------------------------\n" +
                " |help : Print out this help information.\n" +
                " |\n" +
                " |load <input CleanPlan file> :\n" +
                " |    load a configured nadeef clean plan.\n" +
                " |\n" +
                " |detect [rule id] :\n" +
                " |    start the violation detection with a given rule id number.\n" +
                " |\n" +
                " |list : \n" +
                " |    list available rules.\n" +
                " |\n" +
                " |repair <target table name> :\n" +
                " |    repair the data source.\n" +
                " |\n" +
                " |exit :\n" +
                " |    exit the console (Ctrl + D).\n";
        console.println(help);
    }
}

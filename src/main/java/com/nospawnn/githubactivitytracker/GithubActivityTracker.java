package com.nospawnn.githubactivitytracker;

import com.nospawnn.githubactivitytracker.cli.CLI;
import com.nospawnn.githubactivitytracker.web.Web;

public class GithubActivityTracker {
    public static void main(String[] args) {
        // Run web server by default
        if (args.length == 0)
            Web.run();

        switch (args[0]) {
            case "cli" -> tryRunCli(args);
            case "server" -> Web.run();
            default -> System.err.println("Unknown option '" + args[0] + "'");
        }
    }

    public static void tryRunCli(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: " +
                    "\n    java -jar gat.jar cli <username>");
            return;
        }

        new CLI().runForUsername(args[1]);
    }

}

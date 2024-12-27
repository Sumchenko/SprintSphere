package ru.sfedu.sprintspherepk.cli;

import picocli.CommandLine;

public class CLIApplication {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new MainCommand()).execute(args);
        System.exit(exitCode);
    }
}

package com.maxhomann.musicextractor;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "music-extractor",
    description = "A CLI tool to manage and sort music library based on favorites",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    subcommands = {
        ExtractCommand.class,
        DeleteCommand.class
    }
)
public class MusicExtractorCli implements Runnable {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MusicExtractorCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        // Show help when no subcommand is specified
        new CommandLine(this).usage(System.out);
    }
}

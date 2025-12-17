package com.maxhomann.musicextractor;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Command(
    name = "delete",
    description = "Delete files that are not in the preserve list"
)
public class DeleteCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Path to the music library directory to clean"
    )
    private String libraryPath;

    @Option(
        names = {"-p", "--preserve"},
        description = "Path to a file containing paths to preserve (one per line)",
        required = true
    )
    private String preserveFile;

    @Option(
        names = {"-n", "--dry-run"},
        description = "Perform a dry run without actually deleting files"
    )
    private boolean dryRun;

    @Option(
        names = {"-v", "--verbose"},
        description = "Verbose output showing each file being deleted"
    )
    private boolean verbose;

    @Override
    public Integer call() {
        try {
            Path library = Paths.get(libraryPath);
            if (!Files.exists(library) || !Files.isDirectory(library)) {
                System.err.println("Error: Library path does not exist or is not a directory: " + libraryPath);
                return 1;
            }

            Path preserve = Paths.get(preserveFile);
            if (!Files.exists(preserve)) {
                System.err.println("Error: Preserve file does not exist: " + preserveFile);
                return 1;
            }

            // Read paths to preserve
            Set<Path> pathsToPreserve = readPreservePaths(preserve);
            System.err.println("Loaded " + pathsToPreserve.size() + " paths to preserve");

            // Find all files in the library
            List<Path> allFiles = collectAllFiles(library);
            System.err.println("Found " + allFiles.size() + " total files in library");

            // Determine files to delete
            List<Path> filesToDelete = allFiles.stream()
                .filter(file -> !pathsToPreserve.contains(file))
                .collect(Collectors.toList());

            System.err.println("Files to delete: " + filesToDelete.size());

            if (dryRun) {
                System.err.println("\n=== DRY RUN MODE - No files will be deleted ===");
            }

            // Delete files
            int deletedCount = 0;
            int failedCount = 0;

            for (Path file : filesToDelete) {
                try {
                    if (dryRun) {
                        if (verbose) {
                            System.out.println("Would delete: " + file);
                        }
                        deletedCount++;
                    } else {
                        Files.delete(file);
                        if (verbose) {
                            System.out.println("Deleted: " + file);
                        }
                        deletedCount++;
                    }
                } catch (IOException e) {
                    System.err.println("Failed to delete: " + file + " - " + e.getMessage());
                    failedCount++;
                }
            }

            // Clean up empty directories
            if (!dryRun) {
                cleanEmptyDirectories(library);
            }

            System.err.println("\n=== Summary ===");
            System.err.println((dryRun ? "Would delete: " : "Deleted: ") + deletedCount + " files");
            if (failedCount > 0) {
                System.err.println("Failed to delete: " + failedCount + " files");
            }

            return failedCount > 0 ? 1 : 0;
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private Set<Path> readPreservePaths(Path preserveFile) throws IOException {
        return Files.readAllLines(preserveFile).stream()
            .map(String::trim)
            .filter(line -> !line.isEmpty() && !line.startsWith("#"))
            .map(Paths::get)
            .collect(Collectors.toSet());
    }

    private List<Path> collectAllFiles(Path directory) throws IOException {
        try (Stream<Path> stream = Files.walk(directory)) {
            return stream
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        }
    }

    private void cleanEmptyDirectories(Path directory) throws IOException {
        try (Stream<Path> stream = Files.walk(directory)) {
            List<Path> directories = stream
                .filter(Files::isDirectory)
                .sorted(Comparator.reverseOrder()) // Process from deepest to shallowest
                .collect(Collectors.toList());

            for (Path dir : directories) {
                if (!dir.equals(directory) && isEmptyDirectory(dir)) {
                    try {
                        Files.delete(dir);
                        if (verbose) {
                            System.out.println("Deleted empty directory: " + dir);
                        }
                    } catch (IOException e) {
                        // Ignore errors when deleting directories
                    }
                }
            }
        }
    }

    private boolean isEmptyDirectory(Path directory) throws IOException {
        try (Stream<Path> entries = Files.list(directory)) {
            return entries.findAny().isEmpty();
        }
    }
}

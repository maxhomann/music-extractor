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
    name = "extract",
    description = "Extract favorite songs and identify their albums. Outputs paths that should be preserved."
)
public class ExtractCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Path to the music library directory to scan"
    )
    private String libraryPath;

    @Option(
        names = {"-f", "--favorites"},
        description = "Path to a file containing a list of favorite song paths (one per line)",
        required = true
    )
    private String favoritesFile;

    @Option(
        names = {"-o", "--output"},
        description = "Output file to write paths to preserve (defaults to stdout)"
    )
    private String outputFile;

    @Override
    public Integer call() {
        try {
            Path library = Paths.get(libraryPath);
            if (!Files.exists(library) || !Files.isDirectory(library)) {
                System.err.println("Error: Library path does not exist or is not a directory: " + libraryPath);
                return 1;
            }

            Path favorites = Paths.get(favoritesFile);
            if (!Files.exists(favorites)) {
                System.err.println("Error: Favorites file does not exist: " + favoritesFile);
                return 1;
            }

            // Read favorite songs
            Set<Path> favoriteSongs = readFavorites(favorites);
            System.err.println("Loaded " + favoriteSongs.size() + " favorite songs");

            // Find all albums containing favorite songs
            Set<Path> albumsToPreserve = findAlbumsForFavorites(library, favoriteSongs);
            System.err.println("Found " + albumsToPreserve.size() + " albums to preserve");

            // Collect all files in albums to preserve (including favorite songs)
            Set<Path> filesToPreserve = collectFilesInAlbums(albumsToPreserve);
            System.err.println("Total files to preserve: " + filesToPreserve.size());

            // Output results
            List<String> outputLines = filesToPreserve.stream()
                .map(Path::toString)
                .sorted()
                .collect(Collectors.toList());

            if (outputFile != null) {
                Files.write(Paths.get(outputFile), outputLines);
                System.err.println("Wrote output to: " + outputFile);
            } else {
                outputLines.forEach(System.out::println);
            }

            return 0;
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private Set<Path> readFavorites(Path favoritesFile) throws IOException {
        return Files.readAllLines(favoritesFile).stream()
            .map(String::trim)
            .filter(line -> !line.isEmpty() && !line.startsWith("#"))
            .map(Paths::get)
            .collect(Collectors.toSet());
    }

    private Set<Path> findAlbumsForFavorites(Path library, Set<Path> favoriteSongs) throws IOException {
        Set<Path> albums = new HashSet<>();

        for (Path favorite : favoriteSongs) {
            if (!Files.exists(favorite)) {
                System.err.println("Warning: Favorite song does not exist: " + favorite);
                continue;
            }

            // Find the album directory (parent directory of the song)
            Path albumDir = favorite.getParent();
            if (albumDir != null && Files.isDirectory(albumDir)) {
                albums.add(albumDir);
            }
        }

        return albums;
    }

    private Set<Path> collectFilesInAlbums(Set<Path> albums) throws IOException {
        Set<Path> files = new HashSet<>();

        for (Path album : albums) {
            if (!Files.exists(album) || !Files.isDirectory(album)) {
                continue;
            }

            try (Stream<Path> stream = Files.walk(album)) {
                stream.filter(Files::isRegularFile)
                    .forEach(files::add);
            }
        }

        return files;
    }
}

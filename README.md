# Music Extractor

A CLI application to manage and sort a large music library based on favorite songs.

## Features

- **Extract Command**: Identifies favorite songs and their albums, outputting paths that should be preserved
- **Delete Command**: Safely removes music files not in the preserve list
- Supports dry-run mode for safe testing
- Handles album-based organization

## Requirements

- Java 21 or higher
- Maven 3.x (included via Maven Wrapper)

## Building

```bash
./mvnw clean package
```

This creates an executable JAR at `target/music-extractor.jar`.

## Usage

### Extract Favorites

The `extract` command scans your music library and identifies favorite songs and their albums:

```bash
java -jar target/music-extractor.jar extract <library-path> --favorites <favorites-file> [--output <output-file>]
```

**Arguments:**
- `<library-path>`: Path to your music library directory
- `--favorites` or `-f`: Path to a text file containing paths to your favorite songs (one per line)
- `--output` or `-o`: (Optional) Output file to write paths to preserve. If not specified, outputs to stdout

**Example:**
```bash
# Output to file
java -jar target/music-extractor.jar extract /music/library --favorites favorites.txt --output preserve.txt

# Output to stdout
java -jar target/music-extractor.jar extract /music/library --favorites favorites.txt
```

### Delete Non-Favorites

The `delete` command removes files not in the preserve list:

```bash
java -jar target/music-extractor.jar delete <library-path> --preserve <preserve-file> [--dry-run] [--verbose]
```

**Arguments:**
- `<library-path>`: Path to your music library directory
- `--preserve` or `-p`: Path to a text file containing paths to preserve (output from extract command)
- `--dry-run` or `-n`: Perform a dry run without actually deleting files
- `--verbose` or `-v`: Show each file being deleted

**Example:**
```bash
# Dry run first (recommended)
java -jar target/music-extractor.jar delete /music/library --preserve preserve.txt --dry-run --verbose

# Actual deletion
java -jar target/music-extractor.jar delete /music/library --preserve preserve.txt --verbose
```

## Workflow Example

1. **Create a favorites file** (`favorites.txt`) with paths to your favorite songs:
```
/music/library/Artist1/Album1/song1.mp3
/music/library/Artist2/Album2/song2.mp3
```

2. **Extract favorites and their albums**:
```bash
java -jar target/music-extractor.jar extract /music/library --favorites favorites.txt --output preserve.txt
```

3. **Review the preserve list** (optional):
```bash
cat preserve.txt
```

4. **Test deletion with dry-run**:
```bash
java -jar target/music-extractor.jar delete /music/library --preserve preserve.txt --dry-run --verbose
```

5. **Perform actual deletion**:
```bash
java -jar target/music-extractor.jar delete /music/library --preserve preserve.txt --verbose
```

## How It Works

### Album Detection
The application determines albums based on directory structure:
- A song's album is its parent directory
- All files in an album directory are preserved if any song in that album is a favorite

### File Preservation
- All favorite songs are preserved
- All files in albums containing favorites are preserved
- All other files can be safely deleted

## License

MIT License - see LICENSE file for details

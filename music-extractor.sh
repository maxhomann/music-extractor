#!/bin/bash

# Music Extractor Runner Script
# This script ensures the application is built and runs it with Java 21

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_FILE="${SCRIPT_DIR}/target/music-extractor.jar"

# Check if Java 21 is available
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "Error: Java 21 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

# Build if jar doesn't exist
if [ ! -f "$JAR_FILE" ]; then
    echo "Building application..."
    cd "$SCRIPT_DIR"
    ./mvnw clean package -DskipTests
fi

# Run the application
exec java -jar "$JAR_FILE" "$@"

package pl.futurecollars.invoicing.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class FileService {

    private final Path invoicesDbPath;

    public FileService(Path invoicesDbPath) {
        this.invoicesDbPath = invoicesDbPath;
    }

    public void writeToFile(final String line) throws IOException {
        Files.writeString(invoicesDbPath, line + System.lineSeparator(), StandardOpenOption.APPEND);
    }

    public void overwriteTheFile(final String line, StandardOpenOption openOption) throws IOException {
        if (!line.isEmpty()) {
            Files.writeString(invoicesDbPath, line + System.lineSeparator(), openOption);
        }
    }

    public Stream<String> readFile() throws IOException {
        return Files.lines(invoicesDbPath);
    }
}

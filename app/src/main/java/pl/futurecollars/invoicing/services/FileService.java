package pl.futurecollars.invoicing.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.exceptions.FileSystemException;

@Service
public class FileService {

    private final Path invoicesDbPath;

    public FileService(Path invoicesDbPath) {
        this.invoicesDbPath = invoicesDbPath;
    }

    public void writeToFile(final String line) {
        try {
            Files.writeString(invoicesDbPath, line + System.lineSeparator(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new FileSystemException("File system processing error.");
        }
    }

    public void overwriteTheFile(final String line, StandardOpenOption openOption) {
        if (!line.isEmpty()) {
            try {
                Files.writeString(invoicesDbPath, line + System.lineSeparator(), openOption);
            } catch (IOException e) {
                throw new FileSystemException("File system processing error.");
            }
        }
    }

    public Stream<String> readFile() {
        try {
            return Files.lines(invoicesDbPath);
        } catch (IOException e) {
            throw new FileSystemException("File system processing error.");
        }
    }
}

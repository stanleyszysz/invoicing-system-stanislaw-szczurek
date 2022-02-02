package pl.futurecollars.invoicing.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import pl.futurecollars.invoicing.exceptions.FileSystemException;

@Slf4j
public class FileService {

    private Path invoicesDbPath;

    public FileService(String invoicesDbPath) {
        try {
            this.invoicesDbPath = ResourceUtils.getFile(invoicesDbPath).toPath();
        } catch (FileNotFoundException e) {
            log.warn("File exist");
        }
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
        } else {
            try {
                Files.writeString(invoicesDbPath, line, openOption);
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

    public void clear() {
        try {
            //            Files.writeString(FileConfiguration.INVOICES_DB_PATH, "");
            Files.write(invoicesDbPath, "".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package pl.futurecollars.invoicing.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import pl.futurecollars.invoicing.config.FileConfiguration;

public class FileService {

    public void writeToFile(final String line) throws IOException {
        Files.writeString(new File(FileConfiguration.INVOICES_DB_PATH).toPath(), line + System.lineSeparator(), StandardOpenOption.APPEND);
    }

    public void overwriteTheFile(final String line, StandardOpenOption openOption) throws IOException {
        if (!line.isEmpty()) {
            Files.writeString(new File(FileConfiguration.INVOICES_DB_PATH).toPath(), line + System.lineSeparator(), openOption);
        }
    }

    public Stream<String> readFile() throws IOException {
        return Files.lines(new File(FileConfiguration.INVOICES_DB_PATH).toPath());
    }
}

package pl.futurecollars.invoicing.config;

import java.nio.file.Path;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfiguration {

    public static final Path INVOICES_DB_PATH = Path.of("app/src/main/resources/invoices-json.txt");
}

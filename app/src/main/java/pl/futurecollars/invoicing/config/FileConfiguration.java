package pl.futurecollars.invoicing.config;

import java.nio.file.Path;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.db.file.FileBasedInvoiceRepository;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.services.FileService;
import pl.futurecollars.invoicing.services.JsonService;

@Configuration
@ConditionalOnProperty(value = "pl.futurecollars.invoicing.db", havingValue = "file")
public class FileConfiguration {

    public static final Path INVOICES_DB_PATH = Path.of("invoices-json.txt");

    @Bean
    public FileService fileService() {
        return new FileService(INVOICES_DB_PATH);
    }

    @Bean
    public JsonService<Invoice> jsonService() {
        return new JsonService<>();
    }

    @Bean
    public InvoiceRepository invoiceRepository(final FileService fileService, final JsonService<Invoice> jsonService) {
        return new FileBasedInvoiceRepository(fileService, jsonService);
    }
}

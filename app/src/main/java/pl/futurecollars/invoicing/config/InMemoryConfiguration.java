package pl.futurecollars.invoicing.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.db.memory.InMemoryInvoiceRepository;

@Configuration
@ConditionalOnProperty(value = "pl.futurecollars.invoicing.db", havingValue = "memory")
public class InMemoryConfiguration {

    @Bean
    public InvoiceRepository invoiceRepository() {
        return new InMemoryInvoiceRepository();
    }
}

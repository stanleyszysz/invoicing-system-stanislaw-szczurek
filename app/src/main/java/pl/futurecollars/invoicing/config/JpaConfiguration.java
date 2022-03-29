package pl.futurecollars.invoicing.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.db.jpa.JpaInvoiceRepository;
import pl.futurecollars.invoicing.db.jpa.OrmInvoiceRepository;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.services.JsonService;

@Configuration
@ConditionalOnProperty(value = "pl.futurecollars.invoicing.db", havingValue = "jpa")
public class JpaConfiguration {

    @Bean
    public InvoiceRepository ormInvoiceRepository(JpaInvoiceRepository jpaInvoiceRepository) {
        return new OrmInvoiceRepository(jpaInvoiceRepository);
    }

    @Bean
    public JsonService<Invoice> jsonService() {
        return new JsonService<>();
    }
}


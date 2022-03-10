package pl.futurecollars.invoicing.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.db.sql.SqlInvoiceRepository;

@Configuration
@ConditionalOnProperty(value = "pl.futurecollars.invoicing.db", havingValue = "sql")
public class SqlConfiguration {

    @Bean
    public InvoiceRepository invoiceRepository(JdbcTemplate jdbcTemplate) {
        return new SqlInvoiceRepository(jdbcTemplate);
    }
}

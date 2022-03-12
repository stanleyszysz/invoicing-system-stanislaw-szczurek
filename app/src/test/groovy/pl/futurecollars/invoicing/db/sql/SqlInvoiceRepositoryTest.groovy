package pl.futurecollars.invoicing.db.sql

import org.flywaydb.core.Flyway
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.db.InvoiceRepository
import pl.futurecollars.invoicing.db.InvoiceRepositoryTest

import javax.sql.DataSource

@IfProfileValue(name = "spring.profiles.active", value = "sql")
class SqlInvoiceRepositoryTest extends InvoiceRepositoryTest {

    @Override
    InvoiceRepository getRepositoryInstance() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("filesystem:src/test/resources/db/migration")
                .load()

        flyway.clean()
        flyway.migrate()

        def repository = new SqlInvoiceRepository(jdbcTemplate)
        // repository.initVatRatesMap()
        return repository
    }
}
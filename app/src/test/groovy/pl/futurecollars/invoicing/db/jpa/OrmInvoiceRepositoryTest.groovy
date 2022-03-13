package pl.futurecollars.invoicing.db.jpa

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.InvoiceRepository
import pl.futurecollars.invoicing.db.InvoiceRepositoryTest

import javax.sql.DataSource

@DataJpaTest
@ActiveProfiles("jpa")
class OrmInvoiceRepositoryTest extends InvoiceRepositoryTest {

    @Autowired
    private JpaInvoiceRepository jpaInvoiceRepository

    @Override
    InvoiceRepository getRepositoryInstance() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .load()

        flyway.clean()
        flyway.migrate()

        new OrmInvoiceRepository(jpaInvoiceRepository)
    }
}

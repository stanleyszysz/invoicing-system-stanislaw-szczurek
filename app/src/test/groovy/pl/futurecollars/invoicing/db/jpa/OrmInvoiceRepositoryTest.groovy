package pl.futurecollars.invoicing.db.jpa

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.InvoiceRepository
import pl.futurecollars.invoicing.db.InvoiceRepositoryTest

@DataJpaTest
@ActiveProfiles("jpa")
class OrmInvoiceRepositoryTest extends InvoiceRepositoryTest {

    @Autowired
    private JpaInvoiceRepository jpaInvoiceRepository

    @Override
    InvoiceRepository getRepositoryInstance() {

        Flyway flyway = Flyway.configure()
                 .dataSource("jdbc:h2:mem:db", "sa", "")
                 .locations("db/migration")
                 .load()

         flyway.clean()
         flyway.migrate()

        new OrmInvoiceRepository(jpaInvoiceRepository)
    }

}

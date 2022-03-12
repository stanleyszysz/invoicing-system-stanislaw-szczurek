package pl.futurecollars.invoicing.db.jpa

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.db.InvoiceRepository
import pl.futurecollars.invoicing.db.InvoiceRepositoryTest

@DataJpaTest
@IfProfileValue(name = "spring.profiles.active", value = "jpa")
class OrmInvoiceRepositoryTest extends InvoiceRepositoryTest {

    @Autowired
    private JpaInvoiceRepository jpaInvoiceRepository

    @Override
    InvoiceRepository getRepositoryInstance() {
        assert jpaInvoiceRepository != null
        new OrmInvoiceRepository(jpaInvoiceRepository)
    }
}

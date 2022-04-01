package pl.futurecollars.invoicing.db.memory

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.InvoiceRepository
import pl.futurecollars.invoicing.db.InvoiceRepositoryTest

@ActiveProfiles("memory")
@SpringBootTest
class InMemoryInvoiceRepositoryTest extends InvoiceRepositoryTest {

    @Override
    InvoiceRepository getRepositoryInstance() {
        return new InMemoryInvoiceRepository()
    }
}

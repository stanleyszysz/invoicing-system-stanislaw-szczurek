package pl.futurecollars.invoicing.db.file

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.InvoiceRepository
import pl.futurecollars.invoicing.db.InvoiceRepositoryTest

@ActiveProfiles("file")
@SpringBootTest
class FileBasedInvoiceRepositoryTest extends InvoiceRepositoryTest {

    @Autowired
    FileBasedInvoiceRepository fileBasedInvoiceRepository

    @Override
    InvoiceRepository getRepositoryInstance() {
        return fileBasedInvoiceRepository
    }
}

package pl.futurecollars.invoicing.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

@ActiveProfiles("memory")
@AutoConfigureMockMvc
@SpringBootTest
class InvoiceServiceTest extends Specification {

    @Autowired
    private InvoiceService invoiceService

    @Autowired
    Environment environment

    private Invoice invoice1
    private Invoice invoice2
    private Invoice invoice3

    def setup() {
        invoiceService.clear()

        def profile = ""
        if (environment != null) {
            profile = environment.getActiveProfiles()[0]
        }

        invoice1 = TestHelpers.invoice(1, profile)
        invoice2 = TestHelpers.invoice(3, profile)
        invoice3 = TestHelpers.invoice(5, profile)
    }

    def "should save invoices to repository"() {
        when:
        invoiceService.save(invoice1)

        then:
        invoiceService.getById(invoice1.getId()).get().getBuyer().getName() == "Abra 2"
    }

    def "should get invoice by id"() {
        when:
        invoiceService.save(invoice1)
        invoiceService.save(invoice2)

        then:
        invoiceService.getById(invoice1.getId()).isPresent()
        invoiceService.getById(invoice2.getId()).get().getSeller().getName() == "Abra 3"
    }

    def "should get number of invoices in repository"() {
        given:
        invoiceService.save(invoice1)
        invoiceService.save(invoice2)
        invoiceService.save(invoice3)

        expect:
        invoiceService.getAll().size() == 3
    }

    def "should can update invoice"() {
        given:
        invoiceService.save(invoice1)

        when:
        def updateInvoice = invoiceService.update(invoice1.getId(), invoice2)

        then:
        updateInvoice.getSeller().getName() == "Abra 3"
    }

    def "should can delete invoice"() {
        given:
        invoiceService.save(invoice1)
        invoiceService.save(invoice2)
        invoiceService.save(invoice3)

        when:
        invoiceService.delete(invoice3.getId())

        then:
        invoiceService.getAll().size() == 2
    }
}

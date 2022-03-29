package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

abstract class InvoiceRepositoryTest extends Specification {

    InvoiceRepository invoiceRepository = getRepositoryInstance()
    abstract InvoiceRepository getRepositoryInstance()

    private Invoice invoice1
    private Invoice invoice2
    private Invoice invoice3

    @Autowired
    Environment environment

    def setup() {
        invoiceRepository = getRepositoryInstance()
        invoiceRepository.clear()

        def profile = ""
        if (environment != null) {
            profile = environment.getActiveProfiles()[0]
        }

        invoice1 = TestHelpers.invoice(1, profile)
        invoice2 = TestHelpers.invoice(3, profile)
        invoice3 = TestHelpers.invoice(5, profile)
    }

    def "should save invoices"() {
        when:
        def savedInvoice= invoiceRepository.save(invoice1)

        then:
        invoiceRepository.getById(savedInvoice.getId()).isPresent()
    }

    def "should get invoice by id"() {
        when:
        invoiceRepository.save(invoice1)
        invoiceRepository.save(invoice2)

        then:
        invoiceRepository.getById(invoice1.getId()).isPresent()
        invoiceRepository.getById(invoice2.getId()).get().getBuyer().getName() == "Abra 4"
    }

    def "should get number of entries in database"() {
        given:
        invoiceRepository.save(invoice1)
        invoiceRepository.save(invoice2)
        invoiceRepository.save(invoice3)

        expect:
        invoiceRepository.getAll().size() == 3
    }

    def "should can update invoice"() {
        given:
        invoiceRepository.save(invoice1)

        when:
        def updateInvoice = invoiceRepository.update(invoice1.getId(), invoice2)

        then:
        updateInvoice.getSeller().getName() == "Abra 3"
    }

    def "should return exception when can't update invoice"() {
        given:
        invoiceRepository.save(invoice1)

        when:
        invoiceRepository.update(UUID.fromString("9ced63bd-d4f7-4bcf-8e15-2ce6163e9f62"), invoice2)

        then:
        thrown NoSuchElementException
    }

    def "should get number of entries in database after deleting the invoice"() {
        given:
        invoiceRepository.save(invoice1)
        invoiceRepository.save(invoice2)
        invoiceRepository.save(invoice3)

        when:
        invoiceRepository.delete(invoice2.getId())

        then:
        invoiceRepository.getAll().size() == 2
    }

    def "should return exception when id doesn't exist"() {
        when:
        invoiceRepository.delete(UUID.fromString("9ced63bd-d4f7-4bcf-8e15-2ce6163e9f62"))

        then:
        thrown NoSuchElementException
    }

    def "should clear database"() {
        when:
        invoiceRepository.save(invoice1)
        invoiceRepository.save(invoice2)
        invoiceRepository.save(invoice3)

        invoiceRepository.clear()

        then:
        invoiceRepository.getAll().size() == 0
    }
}
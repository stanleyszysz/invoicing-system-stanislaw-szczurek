package pl.futurecollars.invoicing.services

import pl.futurecollars.invoicing.db.memory.InMemoryInvoiceRepository
import pl.futurecollars.invoicing.model.Address
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

import java.time.LocalDate

class InvoiceServiceTest extends Specification {
    def buyer1 = new Company("5252287009", "Torte", new Address("Solec", "05-532", "Słonecznikowa", "8"))
    def buyer2 = new Company("5060111906", "Arcon", new Address("Wisła", "08-540", "Kościuszki", "26"))
    def seller1 = new Company("5891937075", "New Eko", new Address("Żukowo", "83-330", "Witosławy", "20"))
    def seller2 = new Company("9670365949", "Egor", new Address("Bydgoszcz", "85-039", "Hetmańska", "3"))
    def dateAt1 = LocalDate.of(2022, 01, 04)
    def dateAt2 = LocalDate.of(2022, 01, 05)
    def entry1 = new InvoiceEntry("Pen", 99.99, 22.9977, Vat.VAT_23)
    def entry2 = new InvoiceEntry("Shampoo", 29.99, 6.8977, Vat.VAT_23)
    def entry3 = new InvoiceEntry("Teaspoon", 1.95, 0.4485, Vat.VAT_23)
    def entry4 = new InvoiceEntry("Bag", 3.49, 0.8027, Vat.VAT_23)
    def entries1 = Arrays.asList(entry1, entry2)
    def entries2 = Arrays.asList(entry3, entry4)
    def entries3 = Arrays.asList(entry1, entry2, entry3)
    def id1 = UUID.randomUUID()
    def id2 = UUID.randomUUID()
    def id3 = UUID.randomUUID()
    def invoice1 = new Invoice(id1, dateAt1, seller1, buyer1, entries1)
    def invoice2 = new Invoice(id2, dateAt2, seller2, buyer2, entries2)
    def invoice3 = new Invoice(id3, dateAt2, seller1, buyer2, entries3)
    def repository = new InMemoryInvoiceRepository()
    def invoiceService = new InvoiceService(repository)

    def "should save invoices to repository"() {
        when:
        invoiceService.save(invoice1)

        then:
        repository.getById(invoice1.getId()).get().getBuyer().getName() == "Torte"
    }

    def "should get invoice by id"() {
        when:
        invoiceService.save(invoice1)
        invoiceService.save(invoice2)

        then:
        repository.getById(invoice1.getId()).isPresent()
        repository.getById(invoice2.getId()).get().getSeller().getName() == "Egor"
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
        updateInvoice.getSeller().getName() == "Egor"
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

package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.model.Address
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification
import java.time.LocalDate

class InMemoryInvoiceRepositoryTest extends Specification {
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
    def invoice1 = new Invoice(UUID.randomUUID(), dateAt1, seller1, buyer1, entries1)
    def invoice2 = new Invoice(UUID.randomUUID(), dateAt2, seller2, buyer2, entries2)
    def invoice3 = new Invoice(UUID.randomUUID(), dateAt2, seller1, buyer2, entries3)
    def repository = new InMemoryInvoiceRepository()

    def "should save invoices to repository"() {
        when:
        def saveInvoice = repository.save(invoice1)

        then:
        repository.getById(saveInvoice.getId()) != null
    }

    def "should get invoice by id"() {
        when:
        def saveInvoice1 = repository.save(invoice1)
        def saveInvoice2 = repository.save(invoice2)

        then:
        repository.getById(saveInvoice1.getId()).isPresent()
        repository.getById(saveInvoice2.getId()).get().getSeller().getName() == "Egor"
    }

    def "should get number of invoices in repository"() {
        given:
        repository.save(invoice1)
        repository.save(invoice2)
        repository.save(invoice3)

        expect:
        repository.getAll().size() == 3
    }

    def "should can update invoice"() {
        given:
        repository.save(invoice1)

        when:
        def updateInvoice = repository.update(invoice1.getId(), invoice2)

        then:
        updateInvoice.getSeller().getName() == "Egor"
    }

    def "should return exception when can't update invoice"() {
        given:
        repository.save(invoice1)

        when:
        def updateInvoice = repository.update(UUID.fromString("9ced63bd-d4f7-4bcf-8e15-2ce6163e9f62"), invoice2)

        then:
        thrown IllegalArgumentException
    }

    def "should can delete invoice"() {
        given:
        repository.save(invoice1)
        repository.save(invoice2)
        repository.save(invoice3)

        when:
        repository.delete(invoice3.getId())

        then:
        repository.getAll().size() == 2
    }

    def "should return exception when id doesn't exist"() {
        when:
        repository.delete(UUID.fromString("9ced63bd-d4f7-4bcf-8e15-2ce6163e9f62"))

        then:
        thrown IllegalArgumentException
    }
}

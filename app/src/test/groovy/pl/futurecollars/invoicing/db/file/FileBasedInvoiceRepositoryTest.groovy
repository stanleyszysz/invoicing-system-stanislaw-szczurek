package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.exceptions.DbException
import pl.futurecollars.invoicing.model.Address
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import pl.futurecollars.invoicing.services.FileService
import pl.futurecollars.invoicing.services.JsonService
import spock.lang.Specification

import java.time.LocalDate

class FileBasedInvoiceRepositoryTest extends Specification {

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
    def fileDb = File.createTempFile('invoices', '.txt').toPath()
    def fileService = new FileService(fileDb)
    def jsonService = new JsonService<Invoice>()
    def fileRepository = new FileBasedInvoiceRepository(fileService, jsonService)



    def "should save invoices to file"() {
        when:
        fileRepository.save(invoice1)

        then:
        fileRepository.getById(invoice1.getId()).isPresent()
        fileRepository.getById(invoice1.getId()).get().getBuyer().getName() == "Torte"
    }

    def "should return exception when try save invoice with existing id"() {
        when:
        fileRepository.save(invoice1)
        fileRepository.save(invoice1)

        then:
        thrown DbException
    }

    def "should get invoice by id"() {
        when:
        fileRepository.save(invoice1)
        fileRepository.save(invoice2)

        then:
        fileRepository.getById(invoice1.getId()).isPresent()
        fileRepository.getById(invoice2.getId()).get().getBuyer().getName() == "Arcon"
    }

    def "should get number of entries in database file"() {
        given:
        def numberOfLine = fileRepository.getAll().size()
        fileRepository.save(invoice1)
        fileRepository.save(invoice2)
        fileRepository.save(invoice3)

        expect:
        fileRepository.getAll().size() == numberOfLine + 3
    }

    def "should can update invoice"() {
        given:
        fileRepository.save(invoice1)

        when:
        def updateInvoice = fileRepository.update(invoice1.getId(), invoice2)

        then:
        updateInvoice.getSeller().getName() == "Egor"
    }

    def "should get number of entries in database file after deleting the invoice"() {
        given:
        def numberOfLine = fileRepository.getAll().size()
        fileRepository.save(invoice1)
        fileRepository.save(invoice2)
        fileRepository.save(invoice3)

        when:
        fileRepository.delete(invoice2.getId())

        then:
        fileRepository.getAll().size() == numberOfLine + 2
    }
}

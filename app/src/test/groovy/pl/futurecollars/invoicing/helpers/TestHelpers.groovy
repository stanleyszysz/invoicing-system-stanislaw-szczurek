package pl.futurecollars.invoicing.helpers

import pl.futurecollars.invoicing.model.Address
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static buyer1 = new Company("5252287009", "Torte", new Address("Solec", "05-532", "Słonecznikowa", "8"))
    static buyer2 = new Company("5060111906", "Arcon", new Address("Wisła", "08-540", "Kościuszki", "26"))
    static seller1 = new Company("5891937075", "New Eko", new Address("Żukowo", "83-330", "Witosławy", "20"))
    static seller2 = new Company("9670365949", "Egor", new Address("Bydgoszcz", "85-039", "Hetmańska", "3"))
    static dateAt1 = LocalDate.of(2022, 01, 04)
    static dateAt2 = LocalDate.of(2022, 01, 05)
    static entry1 = new InvoiceEntry("Pen", 99.99, 22.9977, Vat.VAT_23)
    static entry2 = new InvoiceEntry("Shampoo", 29.99, 6.8977, Vat.VAT_23)
    static entry3 = new InvoiceEntry("Teaspoon", 1.95, 0.4485, Vat.VAT_23)
    static entry4 = new InvoiceEntry("Bag", 3.49, 0.8027, Vat.VAT_23)
    static entries1 = Arrays.asList(entry1, entry2)
    static entries2 = Arrays.asList(entry3, entry4)
    static entries3 = Arrays.asList(entry1, entry2, entry3)
    static UUID id1 = UUID.randomUUID()

    static invoice1 = new Invoice(id1, dateAt1, seller1, buyer1, entries1)
    static invoice2 = new Invoice(UUID.randomUUID(), dateAt2, seller2, buyer2, entries2)
    static invoice3 = new Invoice(UUID.randomUUID(), dateAt2, seller1, buyer2, entries3)

}

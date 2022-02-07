package pl.futurecollars.invoicing.helpers

import pl.futurecollars.invoicing.model.Address
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static address(int id) {
        Address.builder()
                .city("Wroclaw $id")
                .postalCode("99-99$id")
                .streetName("Słonecznikowa")
                .streetNumber("$id")
                .build()
    }

    static company(int id) {
        Company.builder()
                .taxIdentifier("555555555$id")
                .name("Abra $id")
                .address(TestHelpers.address(id))
                .build()
    }

    static invoiceEntry(int id) {
        InvoiceEntry.builder()
                .description("Product $id")
                .price(BigDecimal.valueOf(id * 1000))
                .vatValue(BigDecimal.valueOf(id * 1000 * 0.08))
                .vatRate(Vat.VAT_8)
                .build()
    }

    static invoice(int id) {
        Invoice.builder()
                .id(UUID.randomUUID())
                .dateAt(LocalDate.now())
                .seller(company(id))
                .buyer(company(id + 1))
                .entries((1..5).collect({ invoiceEntry(it) }))
                .build()
    }
}

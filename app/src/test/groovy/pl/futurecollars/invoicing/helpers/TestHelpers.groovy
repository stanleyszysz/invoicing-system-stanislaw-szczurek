package pl.futurecollars.invoicing.helpers

import pl.futurecollars.invoicing.model.Address
import pl.futurecollars.invoicing.model.Car
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

    static car(int id, boolean personalUsage) {
        Car.builder()
                .registrationNumber("DW 5G88$id")
                .personalUsage(personalUsage)
                .build()
    }

    static company(int id) {
        Company.builder()
                .taxIdentifier("555555555$id")
                .name("Abra $id")
                .address(TestHelpers.address(id))
                .pensionInsurance(BigDecimal.valueOf(1000) * BigDecimal.valueOf(id))
                .healthInsurance(BigDecimal.valueOf(500) * BigDecimal.valueOf(id))
                .build()
    }

    static invoiceEntry(int id) {
        InvoiceEntry.builder()
                .description("Product $id")
                .price(BigDecimal.valueOf(id * 1000))
                .vatValue(BigDecimal.valueOf(id * 1000 * 0.23))
                .vatRate(Vat.VAT_23)
                .carRelatedExpense(car(1, false))
                .build()
    }

    static invoice(int id) {
        Invoice.builder()
                .id(UUID.randomUUID())
                .dateAt(LocalDate.now())
                .number("FA/$id")
                .seller(company(id))
                .buyer(company(id + 1))
                .entries((1..5).collect({ invoiceEntry(it) }))
                .build()
    }
}

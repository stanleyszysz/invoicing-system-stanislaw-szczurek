package pl.futurecollars.invoicing.helpers


import pl.futurecollars.invoicing.model.*

import java.time.LocalDate

class TestHelpers {

    static address(int id, String profile) {
        if (profile != "jpa") {
            Address.builder()
                    .addressId(UUID.randomUUID())
                    .city("Wroclaw $id")
                    .postalCode("99-99$id")
                    .streetName("Słonecznikowa")
                    .streetNumber("$id")
                    .build()
        } else {
            Address.builder()
                    .city("Wroclaw $id")
                    .postalCode("99-99$id")
                    .streetName("Słonecznikowa")
                    .streetNumber("$id")
                    .build()
        }
    }

    static car(int id, boolean personalUsage, String profile) {
        if (profile != "jpa") {
            Car.builder()
                    .id(UUID.randomUUID())
                    .registrationNumber("DW 5G88$id")
                    .personalUsage(personalUsage)
                    .build()
        } else {
            Car.builder()
                    .registrationNumber("DW 5G88$id")
                    .personalUsage(personalUsage)
                    .build()
        }
    }

    static company(int id, String profile) {
        if (profile != "jpa") {
            Company.builder()
                    .companyId(UUID.randomUUID())
                    .taxIdentifier("555555555$id")
                    .name("Abra $id")
                    .address(TestHelpers.address(id, profile))
                    .pensionInsurance(BigDecimal.valueOf(1000) * BigDecimal.valueOf(id))
                    .healthInsurance(BigDecimal.valueOf(500) * BigDecimal.valueOf(id))
                    .build()
        } else {
            Company.builder()
                    .taxIdentifier("555555555$id")
                    .name("Abra $id")
                    .address(TestHelpers.address(id, profile))
                    .pensionInsurance(BigDecimal.valueOf(1000) * BigDecimal.valueOf(id))
                    .healthInsurance(BigDecimal.valueOf(500) * BigDecimal.valueOf(id))
                    .build()
        }
    }

    static invoiceEntry(int id, String profile) {
        if (profile != "jpa") {
            InvoiceEntry.builder()
                    .id(UUID.randomUUID())
                    .description("Product $id")
                    .price(BigDecimal.valueOf(id * 1000))
                    .vatValue(BigDecimal.valueOf(id * 1000 * 0.23))
                    .vatRate(Vat.VAT_23)
                    .carRelatedExpense(car(1, false, profile))
                    .build()
        } else {
            InvoiceEntry.builder()
                    .description("Product $id")
                    .price(BigDecimal.valueOf(id * 1000))
                    .vatValue(BigDecimal.valueOf(id * 1000 * 0.23))
                    .vatRate(Vat.VAT_23)
                    .carRelatedExpense(car(1, false, profile))
                    .build()
        }
    }

    static invoice(int id, String profile) {
        if (profile != "jpa") {
            Invoice.builder()
                    .id(UUID.randomUUID())
                    .dateAt(LocalDate.now())
                    .number("FA/$id")
                    .seller(company(id, profile))
                    .buyer(company(id + 1, profile))
                    .entries((1..5).collect({ invoiceEntry(it, profile) }))
                    .build()
        } else {
            Invoice.builder()
                    .dateAt(LocalDate.now())
                    .number("FA/$id")
                    .seller(company(id, profile))
                    .buyer(company(id + 1, profile))
                    .entries((1..5).collect({ invoiceEntry(it, profile) }))
                    .build()
        }
    }
}

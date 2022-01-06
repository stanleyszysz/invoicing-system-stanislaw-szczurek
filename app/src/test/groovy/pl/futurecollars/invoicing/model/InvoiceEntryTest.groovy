package pl.futurecollars.invoicing.model

import spock.lang.Specification

class InvoiceEntryTest extends Specification {
    def "should be able to add new invoiceEntry"(){

        given:
        def invoiceEntry = new InvoiceEntry("Pen", 100, 23, Vat.VAT_23)

        when:
        def result = invoiceEntry

        then:
        result != null

    }
}

package pl.futurecollars.invoicing.services

import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def "should convert object to json and vice versa"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestHelpers.invoice1

        when:
        def invoiceToString = jsonService.toJson(invoice)

        and:
        def invoiceFromJson = jsonService.toObject(invoiceToString, Invoice)

        then:
        invoice == invoiceFromJson
    }
}

package pl.futurecollars.invoicing.services

import pl.futurecollars.invoicing.exceptions.FileSystemException
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import java.nio.file.StandardOpenOption

class JsonServiceTest extends Specification {

    def jsonService = new JsonService()

    def "should convert object to json and vice versa"() {
        given:
        def invoice = TestHelpers.invoice1

        when:
        def invoiceToString = jsonService.toJson(invoice)

        and:
        def invoiceFromJson = jsonService.toObject(invoiceToString, Invoice)

        then:
        invoice == invoiceFromJson
    }

    def "should returned exception when creating object from empty string"() {
        when:
        jsonService.toObject("", Invoice)

        then:
        thrown FileSystemException
    }
}

package pl.futurecollars.invoicing.model

import spock.lang.Specification

class CompanyTest extends Specification {
    def "should be able to create instance of the class"() {

        given:
        def company = new Company("5252287009", "Torte", new Address("Solec", "05-532", "Słonecznikowa", "8"))

        when:
        def expect = company

        then:
        expect != null

    }
}

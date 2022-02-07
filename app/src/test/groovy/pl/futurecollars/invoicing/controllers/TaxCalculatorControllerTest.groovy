package pl.futurecollars.invoicing.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.db.InvoiceRepository
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.TaxCalculatorResult
import pl.futurecollars.invoicing.services.JsonService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("memory")
@AutoConfigureMockMvc
@SpringBootTest
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    InvoiceRepository invoiceRepository

    private Invoice invoice1 = TestHelpers.invoice(1)
    private Invoice invoice2 = TestHelpers.invoice(3)
    private UUID id

    def setup() {
        invoiceRepository.clear()
        invoice1 = saveInvoice(invoice1)
        invoice2 = saveInvoice(invoice2)
    }

    private Invoice saveInvoice(invoice) {
        def result = mockMvc.perform(post("/invoices")
                .content(jsonService.toJson(invoice))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        invoice = jsonService.toObject(result, Invoice)
        id = invoice.getId()

        return invoice
    }

    private List<Invoice> getAllInvoices() {
        def result = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return jsonService.toObject(result, Invoice[])
    }

    private TaxCalculatorResult taxCalculatorResult(String taxIdentifier) {
        def result = mockMvc.perform(get("/tax/$taxIdentifier"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.toObject(result, TaxCalculatorResult)
    }

    def "should returned zeros when there are no invoices with tax identifier"() {
        when:
        def taxCalculating = taxCalculatorResult("0000000000")

        then:
        taxCalculating.income == 0
        taxCalculating.costs == 0
        taxCalculating.earnings == 0
        taxCalculating.incomingVat == 0
        taxCalculating.outgoingVat == 0
        taxCalculating.vatToPay == 0
    }

    def "should returned all results when seller tax identifier is matching"() {
        when:
        def taxCalculating = taxCalculatorResult("5555555551")

        then:
        taxCalculating.income == 15000
        taxCalculating.costs == 0
        taxCalculating.earnings == 15000
        taxCalculating.incomingVat == 1200
        taxCalculating.outgoingVat == 0
        taxCalculating.vatToPay == 1200
    }

    def "should returned all results when buyer tax identifier is matching"() {
        when:
        def taxCalculating = taxCalculatorResult("5555555552")

        then:
        taxCalculating.income == 0
        taxCalculating.costs == 15000
        taxCalculating.earnings == -15000
        taxCalculating.incomingVat == 0
        taxCalculating.outgoingVat == 1200
        taxCalculating.vatToPay == -1200
    }
}

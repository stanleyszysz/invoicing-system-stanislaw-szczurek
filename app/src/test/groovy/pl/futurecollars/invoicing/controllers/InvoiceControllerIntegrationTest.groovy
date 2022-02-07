package pl.futurecollars.invoicing.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.db.InvoiceRepository
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.services.JsonService
import spock.lang.Specification


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("file")
@AutoConfigureMockMvc
@SpringBootTest
class InvoiceControllerIntegrationTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    private InvoiceRepository invoiceRepository

    private Invoice invoice1 = TestHelpers.invoice(1)
    private Invoice invoice2 = TestHelpers.invoice(3)
    private Invoice invoice3 = TestHelpers.invoice(5)
    private Company seller2 = TestHelpers.company(7)
    private UUID id

    def setup() {
        invoiceRepository.clear()
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

    def "three invoices is returned after three invoices are saved"() {
        given:
        saveInvoice(invoice1)
        saveInvoice(invoice2)
        saveInvoice(invoice3)

        expect:
        getAllInvoices().size() == 3
    }

    def "seller is returned correctly when getting by id"() {
        given:
        saveInvoice(invoice1)

        when:
        def result = mockMvc.perform(get("/invoices/" + id))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        result.contains("Abra 1")
    }

    def "GetById returned null when id doesn't exist"() {
        when:
        def result = mockMvc.perform(get("/invoices/1f8a9cd9-629c-4a2e-aee3-cf7d90b17712"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def returnedInvoice = jsonService.toObject(result, Invoice)

        then:
        returnedInvoice == null
    }

    def "no invoices are returned when no invoices were added"() {
        expect:
        getAllInvoices().size() == 0
    }

    def "updated seller is returned correctly when updating invoice by id"() {
        given:
        saveInvoice(invoice1)
        invoice1.setSeller(seller2)

        when:
        def result = mockMvc.perform(patch("/invoices/" + id)
                .content(jsonService.toJson(invoice1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        result.contains("Abra 7")
    }

    def "two invoices remain when one of the three is deleted"() {
        given:
        invoice1 = saveInvoice(invoice1)
        invoice2 = saveInvoice(invoice2)
        invoice3 = saveInvoice(invoice3)

        when:
        mockMvc.perform(delete("/invoices/" + invoice2.getId()))

        then:
        getAllInvoices().size() == 2
    }

    def "NoContent returned when remove a invoice with a non-existent id"() {
        expect:
        mockMvc.perform(delete("/invoices/1f8a9cd9-629c-4a2e-aee3-cf7d90b17712"))
                .andExpect(status().isNoContent())
    }
}

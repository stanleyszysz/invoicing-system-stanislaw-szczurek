package pl.futurecollars.invoicing.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.config.FileConfiguration
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.services.JsonService
import spock.lang.Specification

import java.nio.file.Files

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

    private Invoice invoice1 = TestHelpers.invoice1
    private Invoice invoice2 = TestHelpers.invoice2
    private Invoice invoice3 = TestHelpers.invoice3
    private Company seller2 = TestHelpers.seller2

    def setup() {
        Files.writeString((FileConfiguration.INVOICES_DB_PATH), "")
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

    def "invoice is returned correctly when getting by id"() {
        given:
        saveInvoice(invoice1)

        when:
        def result = mockMvc.perform(get("/invoices/" + invoice1.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def returnedInvoice = jsonService.toObject(result, Invoice)

        then:
        returnedInvoice == invoice1
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

    def "invoice is returned correctly when updating by id"() {
        given:
        saveInvoice(invoice1)
        invoice1.setSeller(seller2)

        when:
        def result = mockMvc.perform(patch("/invoices/" + invoice1.getId())
                .content(jsonService.toJson(invoice1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def returnedInvoice = jsonService.toObject(result, Invoice)

        then:
        result.contains("Egor")
        returnedInvoice == invoice1
    }

    def "two invoices remain when one of the three is deleted"() {
        given:
        saveInvoice(invoice1)
        saveInvoice(invoice2)
        saveInvoice(invoice3)

        when:
        mockMvc.perform(delete("/invoices/" + invoice2.getId()))

        then:
        getAllInvoices().size() == 2
    }

    def "NoContent is returned when remove a invoice with a non-existent id"() {
        expect:
        mockMvc.perform(delete("/invoices/1f8a9cd9-629c-4a2e-aee3-cf7d90b17712"))
                .andExpect(status().isNoContent())
    }
}

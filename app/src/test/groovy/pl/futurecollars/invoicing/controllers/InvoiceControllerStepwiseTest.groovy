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
import spock.lang.Stepwise

import java.nio.file.Files

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("file")
@AutoConfigureMockMvc
@SpringBootTest
@Stepwise
class InvoiceControllerStepwiseTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    private Invoice invoice1 = TestHelpers.invoice1
    private Company seller2 = TestHelpers.seller2

    def "empty array is returned when no invoices were saved"() {
        given:
        Files.writeString((FileConfiguration.INVOICES_DB_PATH), "")
        String expectedBody = "[]"

        when:
        String result = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        expectedBody == result
    }


    def "add single invoice with seller New Eko"() {
        when:
        def result = mockMvc.perform(post("/invoices")
                .content(jsonService.toJson(invoice1))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoice = jsonService.toObject(result, Invoice)

        then:
        invoice == invoice1
    }

    def "one invoice is returned when getting all invoices"() {

        when:
        def result = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonService.toObject(result, Invoice[])

        then:
        invoices.size() == 1
    }

    def "invoice is returned correctly when getting by id"() {
        when:
        def result = mockMvc.perform(get("/invoices/" + TestHelpers.id1))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def savedInvoice = jsonService.toObject(result, Invoice)

        then:
        savedInvoice == invoice1
    }

    def "updated invoice is returned correctly when getting by id"() {
        given:
        invoice1.setSeller(seller2)

        when:
        def result = mockMvc.perform(patch("/invoices/" + TestHelpers.id1)
                .content(jsonService.toJson(invoice1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        result.contains("Egor")
    }

    def "invoice can be deleted"() {
        expect:
        mockMvc.perform(delete("/invoices/" + TestHelpers.id1))
                .andExpect(status().isNoContent())
    }
}

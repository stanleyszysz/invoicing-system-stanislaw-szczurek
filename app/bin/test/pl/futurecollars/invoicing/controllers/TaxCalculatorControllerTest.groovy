package pl.futurecollars.invoicing.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.db.InvoiceRepository
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.*
import pl.futurecollars.invoicing.services.JsonService
import spock.lang.Specification

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.helpers.TestHelpers.company
import static pl.futurecollars.invoicing.helpers.TestHelpers.invoiceEntry

@ActiveProfiles("file")
@AutoConfigureMockMvc
@SpringBootTest
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    Environment environment

    @Autowired
    InvoiceRepository invoiceRepository

    private Invoice invoice1
    private Invoice invoice2
    private UUID id

    def setup() {
        invoiceRepository.clear()

        def profile = ""
        if (environment != null) {
            profile = environment.getActiveProfiles()[0]
        }

        invoice1 = TestHelpers.invoice(1, profile)
        invoice2 = TestHelpers.invoice(3, profile)

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

    private TaxCalculatorResult taxCalculatorResult(Company company) {
        def result = mockMvc.perform(post("/tax")
                .content(jsonService.toJson(company))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.toObject(result, TaxCalculatorResult)
    }

    def "should returned zeros when company id is zero"() {
        when:
        def taxCalculating = taxCalculatorResult(company(0, ""))

        then:
        taxCalculating.income == 0
        taxCalculating.costs == 0
        taxCalculating.incomeMinusCosts == 0
        taxCalculating.pensionInsurance == 0
        taxCalculating.incomeMinusCostsMinusPensionInsurance == 0
        taxCalculating.incomeMinusCostsMinusPensionInsuranceRounded == 0
        taxCalculating.incomeTax == 0.00
        taxCalculating.healthInsurancePaid == 0
        taxCalculating.healthInsuranceToDeduction == 0.00
        taxCalculating.incomeTaxMinusHealthInsurance == 0.0
        taxCalculating.finalIncomeTax == 0
        taxCalculating.incomingVat == 0
        taxCalculating.outgoingVat == 0
        taxCalculating.vatToPay == 0
    }

    def "should returned all results when seller tax identifier is matching"() {
        when:
        def taxCalculating = taxCalculatorResult(company(1, ""))

        then:
        taxCalculating.income == 15000
        taxCalculating.costs == 0
        taxCalculating.incomeMinusCosts == 15000
        taxCalculating.pensionInsurance == 1000
        taxCalculating.incomeMinusCostsMinusPensionInsurance == 14000
        taxCalculating.incomeMinusCostsMinusPensionInsuranceRounded == 14000
        taxCalculating.incomeTax == 2660.00
        taxCalculating.healthInsurancePaid == 500
        taxCalculating.healthInsuranceToDeduction == 430.56
        taxCalculating.incomeTaxMinusHealthInsurance == 2229.44
        taxCalculating.finalIncomeTax == 2229
        taxCalculating.incomingVat == 3450
        taxCalculating.outgoingVat == 0
        taxCalculating.vatToPay == 3450
    }

    def "should returned all results when buyer tax identifier is matching"() {
        when:
        def taxCalculating = taxCalculatorResult(company(2, ""))

        then:
        taxCalculating.income == 0
        taxCalculating.costs == 15000
        taxCalculating.incomeMinusCosts == -15000
        taxCalculating.pensionInsurance == 2000
        taxCalculating.incomeMinusCostsMinusPensionInsurance == -17000.0
        taxCalculating.incomeMinusCostsMinusPensionInsuranceRounded == -17000
        taxCalculating.incomeTax == -3230.0
        taxCalculating.healthInsurancePaid == 1000
        taxCalculating.healthInsuranceToDeduction == 861.11
        taxCalculating.incomeTaxMinusHealthInsurance == -4091.11
        taxCalculating.finalIncomeTax == -4091
        taxCalculating.incomingVat == 0
        taxCalculating.outgoingVat == 3450
        taxCalculating.vatToPay == -3450
    }

    def "tax calculation should be correctly when the car is used for private"() {
        given:
        def invoice = Invoice.builder()
                .id(UUID.randomUUID())
                .dateAt(LocalDate.now())
                .seller(company(5, ""))
                .buyer(company(6, ""))
                .entries(List.of(
                        InvoiceEntry.builder()
                                .price(BigDecimal.valueOf(1000))
                                .vatValue(BigDecimal.valueOf(230))
                                .carRelatedExpense(
                                        Car.builder()
                                                .personalUsage(true)
                                                .build()
                                )
                                .build()
                ))
                .build()

        saveInvoice(invoice)

        when:
        def taxCalculating = taxCalculatorResult(invoice.getBuyer())

        then:
        taxCalculating.income == 0
        taxCalculating.costs == 1115.00
        taxCalculating.incomeMinusCosts == -1115.00
        taxCalculating.incomingVat == 0
        taxCalculating.outgoingVat == 115.00
        taxCalculating.vatToPay == -115.00
    }

    def "tax settlement are executed correctly"() {
        given:
        def companyToTaxSettlement = Company.builder()
                .taxIdentifier("9999999999")
                .pensionInsurance(1000.00)
                .healthInsurance(500.00)
                .build()

        def salesInvoice = Invoice.builder()
                .seller(companyToTaxSettlement)
                .buyer(company(1, ""))
                .entries(List.of(invoiceEntry(1, "")))
                .build()

        def purchaseInvoice = Invoice.builder()
                .seller(company(2, ""))
                .buyer(companyToTaxSettlement)
                .entries(List.of(invoiceEntry(2, "")))
                .build()

        saveInvoice(salesInvoice)
        saveInvoice(purchaseInvoice)

        when:
        def taxCalculatorResponse = taxCalculatorResult(companyToTaxSettlement)

        then:
        with(taxCalculatorResponse) {
            income == 1000
            costs == 2000
            incomeMinusCosts == -1000
            pensionInsurance == 1000.00
            incomeMinusCostsMinusPensionInsurance == -2000
            incomeMinusCostsMinusPensionInsuranceRounded == -2000
            incomeTax == -380.00
            healthInsurancePaid == 500.00
            healthInsuranceToDeduction == 430.5556
            incomeTaxMinusHealthInsurance == -810.5556
            finalIncomeTax == -810

            incomingVat == 230.0
            outgoingVat == 460.0
            vatToPay == -230.0
        }
    }
}

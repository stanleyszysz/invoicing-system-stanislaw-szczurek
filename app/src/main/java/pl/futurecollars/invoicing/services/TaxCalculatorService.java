package pl.futurecollars.invoicing.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.TaxCalculatorResult;

@Service
@RequiredArgsConstructor
public class TaxCalculatorService {

    private final InvoiceRepository invoiceRepository;

    public BigDecimal income(String taxIdentificationNumber) {
        return invoiceRepository.visitStream(invoice -> invoice.getSeller().getTaxIdentifier().equals(taxIdentificationNumber),
            InvoiceEntry::getPrice);
    }

    public BigDecimal costs(String taxIdentificationNumber) {
        return invoiceRepository.visitStream(invoice -> invoice.getBuyer().getTaxIdentifier().equals(taxIdentificationNumber),
            this::getIncomeValueAccordingToPersonalCarUsage);
    }

    private BigDecimal getIncomeValueAccordingToPersonalCarUsage(InvoiceEntry invoiceEntry) {
        return invoiceEntry.getPrice()
            .add(invoiceEntry.getVatValue())
            .subtract(getVatValueAccordingToPersonalCarUsage(invoiceEntry));
    }

    public BigDecimal earnings(String taxIdentificationNumber) {
        return income(taxIdentificationNumber).subtract(costs(taxIdentificationNumber));
    }

    public BigDecimal incomingVat(String taxIdentificationNumber) {
        return invoiceRepository.visitStream(invoice -> invoice.getSeller().getTaxIdentifier().equals(taxIdentificationNumber),
            InvoiceEntry::getVatValue);
    }

    public BigDecimal outgoingVat(String taxIdentificationNumber) {
        return invoiceRepository.visitStream(invoice -> invoice.getBuyer().getTaxIdentifier().equals(taxIdentificationNumber),
            this::getVatValueAccordingToPersonalCarUsage);
    }

    private BigDecimal getVatValueAccordingToPersonalCarUsage(InvoiceEntry invoiceEntry) {
        if (invoiceEntry.getCarRelatedExpense().isPersonalUsage()) {
            return invoiceEntry.getVatValue()
                .multiply(BigDecimal.valueOf(5, 1))
                .setScale(2, RoundingMode.FLOOR);
        } else {
            return invoiceEntry.getVatValue();
        }
    }

    public BigDecimal vatToPay(String taxIdentificationNumber) {
        return incomingVat(taxIdentificationNumber).subtract(outgoingVat(taxIdentificationNumber));
    }

    public TaxCalculatorResult taxCalculatorResult(Company company) {
        String taxIdentificationNumber = company.getTaxIdentifier();

        BigDecimal incomeMinusCosts = earnings(taxIdentificationNumber);
        BigDecimal incomeMinusCostsMinusPensionInsurance = incomeMinusCosts.subtract(company.getPensionInsurance());
        BigDecimal incomeMinusCostsMinusPensionInsuranceRounded = incomeMinusCostsMinusPensionInsurance.setScale(0, RoundingMode.HALF_DOWN);
        BigDecimal incomeTax = incomeMinusCostsMinusPensionInsuranceRounded.multiply(BigDecimal.valueOf(19, 2));
        BigDecimal healthInsuranceToDeduction =
            company.getHealthInsurance().multiply(BigDecimal.valueOf(7.75)).divide(BigDecimal.valueOf(9.0), RoundingMode.HALF_UP);
        BigDecimal incomeTaxMinusHealthInsurance = incomeTax.subtract(healthInsuranceToDeduction);

        return TaxCalculatorResult.builder()
            .income(income(taxIdentificationNumber))
            .costs(costs(taxIdentificationNumber))
            .incomeMinusCosts(incomeMinusCosts)
            .incomeMinusCosts(incomeMinusCosts)
            .pensionInsurance(company.getPensionInsurance())
            .incomeMinusCostsMinusPensionInsurance(incomeMinusCostsMinusPensionInsurance)
            .incomeMinusCostsMinusPensionInsuranceRounded(incomeMinusCostsMinusPensionInsuranceRounded)
            .incomeTax(incomeTax)
            .healthInsurancePaid(company.getHealthInsurance())
            .healthInsuranceToDeduction(healthInsuranceToDeduction)
            .incomeTaxMinusHealthInsurance(incomeTaxMinusHealthInsurance)
            .finalIncomeTax(incomeTaxMinusHealthInsurance.setScale(0, RoundingMode.DOWN))
            .incomingVat(incomingVat(taxIdentificationNumber))
            .outgoingVat(outgoingVat(taxIdentificationNumber))
            .vatToPay(vatToPay(taxIdentificationNumber))
            .build();
    }
}

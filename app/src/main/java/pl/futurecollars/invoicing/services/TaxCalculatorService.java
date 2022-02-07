package pl.futurecollars.invoicing.services;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.InvoiceRepository;
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
            InvoiceEntry::getPrice);
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
            InvoiceEntry::getVatValue);
    }

    public BigDecimal vatToPay(String taxIdentificationNumber) {
        return incomingVat(taxIdentificationNumber).subtract(outgoingVat(taxIdentificationNumber));
    }

    public TaxCalculatorResult taxCalculatorResult(String taxIdentificationNumber) {
        return TaxCalculatorResult.builder()
            .income(income(taxIdentificationNumber))
            .costs(costs(taxIdentificationNumber))
            .earnings(earnings(taxIdentificationNumber))
            .incomingVat(incomingVat(taxIdentificationNumber))
            .outgoingVat(outgoingVat(taxIdentificationNumber))
            .vatToPay(vatToPay(taxIdentificationNumber))
            .build();
    }
}

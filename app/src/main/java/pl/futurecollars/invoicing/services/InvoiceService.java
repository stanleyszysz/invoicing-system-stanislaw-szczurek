package pl.futurecollars.invoicing.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.model.Invoice;

public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Optional<Invoice> getById(UUID id) throws IOException {
        return invoiceRepository.getById(id);
    }

    public List<Invoice> getAll() {
        return invoiceRepository.getAll();
    }

    public Invoice update(UUID id, Invoice updatedInvoice) throws IOException {
        return invoiceRepository.update(id, updatedInvoice);
    }

    public void delete(UUID id) throws IOException {
        invoiceRepository.delete(id);
    }
}

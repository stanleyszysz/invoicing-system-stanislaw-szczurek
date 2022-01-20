package pl.futurecollars.invoicing.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.model.Invoice;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Optional<Invoice> getById(UUID id) {
        return invoiceRepository.getById(id);
    }

    public List<Invoice> getAll() {
        return invoiceRepository.getAll();
    }

    public Invoice update(UUID id, Invoice updatedInvoice) {
        return invoiceRepository.update(id, updatedInvoice);
    }

    public void delete(UUID id) {
        invoiceRepository.delete(id);
    }
}

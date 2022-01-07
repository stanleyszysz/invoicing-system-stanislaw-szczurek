package pl.futurecollars.invoicing.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import pl.futurecollars.invoicing.db.Repository;
import pl.futurecollars.invoicing.model.Invoice;

public class InvoiceService {

    private final Repository repository;

    public InvoiceService(Repository repository) {
        this.repository = repository;
    }

    public Invoice save(Invoice invoice) {
        return repository.save(invoice);
    }

    public Optional<Invoice> getById(UUID id) {
        return repository.getById(id);
    }

    public List<Invoice> getAll() {
        return repository.getAll();
    }

    public Invoice update(UUID id, Invoice updatedInvoice) {
        return repository.update(id, updatedInvoice);
    }

    public void delete(UUID id) {
        repository.delete(id);
    }
}

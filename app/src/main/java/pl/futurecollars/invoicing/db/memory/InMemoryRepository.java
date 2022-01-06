package pl.futurecollars.invoicing.db.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import pl.futurecollars.invoicing.db.Repository;
import pl.futurecollars.invoicing.model.Invoice;

public class InMemoryRepository implements Repository {

    private Map<UUID, Invoice> invoices = new HashMap<>();

    @Override
    public Invoice save(Invoice invoice) {

        UUID id = UUID.randomUUID();

        if (invoices.get(id) == null) {
            invoice.setId(id);
            invoices.put(id, invoice);
            return invoice;
        } else {
            return save(invoice);
        }
    }

    @Override
    public Optional<Invoice> getById(UUID id) {
        return Optional.ofNullable(invoices.get(id));
    }

    @Override
    public List<Invoice> getAll() {
        return new ArrayList<>(invoices.values());
    }

    @Override
    public Invoice update(UUID id, Invoice updatedInvoice) {
        if (invoices.get(id) != null) {
            invoices.replace(id, updatedInvoice);
            return updatedInvoice;
        } else {
            throw new IllegalArgumentException("Id" + id + "doesn't exist.");
        }
    }

    @Override
    public void delete(UUID id) {
        if (invoices.get(id) != null) {
            invoices.remove(id);
        } else {
            throw new IllegalArgumentException("Id" + id + "doesn't exist.");
        }
    }
}

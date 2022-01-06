package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import pl.futurecollars.invoicing.model.Invoice;

public interface Repository {

    Invoice save(Invoice invoice);

    Optional<Invoice> getById(UUID id);

    List<Invoice> getAll();

    Invoice update(UUID id, Invoice updatedInvoice);

    void delete(UUID id);
}

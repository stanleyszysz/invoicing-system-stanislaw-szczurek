package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.model.Invoice;

@Repository
public interface InvoiceRepository {

    Invoice save(Invoice invoice);

    Optional<Invoice> getById(UUID id);

    List<Invoice> getAll();

    Invoice update(UUID id, Invoice updatedInvoice);

    void delete(UUID id);
}

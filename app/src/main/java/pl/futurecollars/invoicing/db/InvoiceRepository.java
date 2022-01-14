package pl.futurecollars.invoicing.db;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import pl.futurecollars.invoicing.model.Invoice;

public interface InvoiceRepository {

    Invoice save(Invoice invoice);

    Optional<Invoice> getById(UUID id) throws IOException;

    List<Invoice> getAll();

    Invoice update(UUID id, Invoice updatedInvoice) throws IOException;

    void delete(UUID id) throws IOException;
}

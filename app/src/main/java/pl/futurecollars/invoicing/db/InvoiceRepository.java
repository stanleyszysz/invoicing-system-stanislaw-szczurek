package pl.futurecollars.invoicing.db;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@Repository
public interface InvoiceRepository {

    Invoice save(Invoice invoice);

    Optional<Invoice> getById(UUID id);

    List<Invoice> getAll();

    Invoice update(UUID id, Invoice updatedInvoice);

    void delete(UUID id);

    void clear();

    default BigDecimal visitStream(Predicate<Invoice> invoice, Function<InvoiceEntry, BigDecimal> invoiceEntryToAmount) {
        return getAll().stream()
            .filter(invoice)
            .flatMap(i -> i.getEntries().stream())
            .map(invoiceEntryToAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

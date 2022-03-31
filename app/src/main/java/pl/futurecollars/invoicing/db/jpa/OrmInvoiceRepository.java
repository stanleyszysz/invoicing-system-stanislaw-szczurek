package pl.futurecollars.invoicing.db.jpa;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.model.Invoice;

@RequiredArgsConstructor
public class OrmInvoiceRepository implements InvoiceRepository {

    @Autowired
    private final JpaInvoiceRepository jpaInvoiceRepository;

    @Override
    public Invoice save(Invoice invoice) {
        invoice.updateRelations();
        return jpaInvoiceRepository.save(invoice);
    }

    @Override
    public Optional<Invoice> getById(UUID id) {
        return Optional.of(jpaInvoiceRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException("Invoice with id: " + id + " doesn't exist.")));
    }

    @Override
    public List<Invoice> getAll() {
        return jpaInvoiceRepository.findAll();
    }

    @Override
    public Invoice update(UUID id, Invoice updatedInvoice) {
        if (jpaInvoiceRepository.findById(id).isPresent()) {
            updatedInvoice.updateRelations();
            return jpaInvoiceRepository.save(updatedInvoice);
        } else {
            throw new NoSuchElementException("Invoice with id: " + id + " doesn't exist.");
        }
    }

    @Override
    public void delete(UUID id) {
        if (jpaInvoiceRepository.findById(id).isPresent()) {
            jpaInvoiceRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Invoice with id: " + id + " doesn't exist.");
        }
    }

    @Override
    public void clear() {
        jpaInvoiceRepository.deleteAll();
    }
}

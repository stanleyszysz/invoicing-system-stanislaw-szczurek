package pl.futurecollars.invoicing.db.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.model.Invoice;

@Repository
public interface JpaInvoiceRepository extends CrudRepository<Invoice, UUID> {

    List<Invoice> findAll();
}

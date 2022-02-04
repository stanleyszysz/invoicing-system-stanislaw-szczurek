package pl.futurecollars.invoicing.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.services.InvoiceService;

@Slf4j
@RestController
@RequestMapping(path = "/invoices", produces = {"application/json;charset=UTF-8"})
@RequiredArgsConstructor
public class InvoiceController implements InvoiceControllerApi {

    private final InvoiceService invoiceService;

    @Override
    @PostMapping
    public ResponseEntity<Invoice> save(@RequestBody Invoice invoice) {
        invoice.generatedId();
        log.debug("Adding new invoice");
        return ResponseEntity.ok(invoiceService.save(invoice));
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<Invoice>> getById(@PathVariable UUID id) {
        log.debug("Getting invoice by id: " + id);
        return ResponseEntity.ok(invoiceService.getById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Invoice>> getAll() {
        log.debug("Getting list of all invoice");
        return ResponseEntity.ok(invoiceService.getAll());
    }

    @Override
    @PatchMapping(path = "/{id}")
    public ResponseEntity<Invoice> update(@PathVariable UUID id, @RequestBody Invoice updatedInvoice) {
        log.debug("Updating invoice by id: " + id);
        return ResponseEntity.ok(invoiceService.update(id, updatedInvoice));
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            invoiceService.delete(id);
        } catch (IllegalArgumentException e) {
            log.debug("Deleting invoice by id: " + id);
            return ResponseEntity.status(204).build();
        }
        log.debug("Cannot delete invoice by id: " + id);
        return ResponseEntity.noContent().build();
    }
}

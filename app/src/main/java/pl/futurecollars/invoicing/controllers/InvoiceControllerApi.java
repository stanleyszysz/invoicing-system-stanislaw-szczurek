package pl.futurecollars.invoicing.controllers;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.futurecollars.invoicing.model.Invoice;

public interface InvoiceControllerApi {

    @PostMapping
    @Operation(summary = "Save invoice", description = "Save new invoice")
    ResponseEntity<Invoice> save(@RequestBody Invoice invoice);

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get invoice by id", description = "Get invoice selected by id")
    ResponseEntity<Optional<Invoice>> getById(@PathVariable UUID id);

    @GetMapping
    @Operation(summary = "Get list of all invoices", description = "Get list of all invoices")
    ResponseEntity<List<Invoice>> getAll();

    @PatchMapping(path = "/{id}")
    @Operation(summary = "Update invoice", description = "Update invoice selected by id")
    ResponseEntity<Invoice> update(@PathVariable UUID id, @RequestBody Invoice updatedInvoice);

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete invoice", description = "Delete invoice selected by id")
    ResponseEntity<Void> delete(@PathVariable UUID id);
}

package pl.futurecollars.invoicing.db.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.exceptions.FileSystemException;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.services.FileService;
import pl.futurecollars.invoicing.services.JsonService;

public class FileBasedInvoiceRepository implements InvoiceRepository {

    private final FileService fileService = new FileService();
    private final JsonService<Invoice> jsonService = new JsonService<>();

    @Override
    public Invoice save(Invoice invoice) {
        UUID id = UUID.randomUUID();
        try {
            if (!this.getById(id).isPresent()) {
                invoice.setId(id);
                String json = jsonService.toJson(invoice);
                fileService.writeToFile(json);
                return invoice;
            } else {
                return save(invoice);
            }
        } catch (IOException e) {
            throw new FileSystemException("File system processing error.");
        }
    }

    @Override
    public Optional<Invoice> getById(UUID id) {
        List<Invoice> invoices;
        try {
            invoices = fileService.readFile()
                .map(item -> {
                    try {
                        return jsonService.toObject(item, Invoice.class);
                    } catch (JsonProcessingException e) {
                        throw new FileSystemException("Json processing error.");
                    }
                }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileSystemException("File system processing error.");
        }

        for (Invoice invoice : invoices) {
            if (invoice.getId().equals(id)) {
                return Optional.ofNullable(invoice);
            }
        }
        return Optional.ofNullable(null);
    }

    @Override
    public List<Invoice> getAll() {
        try {
            return fileService.readFile()
                .map(item -> {
                    try {
                        return jsonService.toObject(item, Invoice.class);
                    } catch (JsonProcessingException e) {
                        throw new FileSystemException("Json processing error.");
                    }
                }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileSystemException("File system processing error.");
        }
    }

    @Override
    public Invoice update(UUID id, Invoice updatedInvoice) {
        List<Invoice> invoices;
        try {
            invoices = fileService.readFile()
                .map(item -> {
                    try {
                        return jsonService.toObject(item, Invoice.class);
                    } catch (JsonProcessingException e) {
                        throw new FileSystemException("Json processing error.");
                    }
                }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileSystemException("File system processing error.");
        }

        for (Invoice invoice : invoices) {
            if (invoice.getId().equals(id)) {
                int index = invoices.indexOf(invoice);
                invoices.set(index, updatedInvoice);
                return updatedInvoice;
            }
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        List<Invoice> invoices;
        try {
            invoices = fileService.readFile()
                .map(item -> {
                    try {
                        return jsonService.toObject(item, Invoice.class);
                    } catch (JsonProcessingException e) {
                        throw new FileSystemException("Json processing error.");
                    }
                }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileSystemException("File system processing error.");
        }

        invoices.removeIf(item -> item.getId().equals(id));

        try {
            fileService.overwriteTheFile(invoices.stream()
                .map(item -> {
                    try {
                        return jsonService.toJson(item);
                    } catch (JsonProcessingException e) {
                        throw new FileSystemException("Json processing error.");
                    }
                }).collect(Collectors.joining(System.lineSeparator())), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new FileSystemException("File system processing error.");
        }
    }
}

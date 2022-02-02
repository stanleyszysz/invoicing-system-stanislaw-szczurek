package pl.futurecollars.invoicing.db.file;

import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.exceptions.DbException;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.services.FileService;
import pl.futurecollars.invoicing.services.JsonService;

public class FileBasedInvoiceRepository implements InvoiceRepository {

    //    @Value(value = "${invoicing.system.stanislaw.szczurek.db.file.dir}");
    //    private Path dbPath;

    private final FileService fileService;
    private final JsonService<Invoice> jsonService;

    public FileBasedInvoiceRepository(FileService fileService, JsonService<Invoice> jsonService) {
        this.fileService = fileService;
        this.jsonService = jsonService;
    }

    @Override
    public Invoice save(Invoice invoice) {
        UUID id = invoice.getId();
        if (this.getById(id).isEmpty()) {
            String json = jsonService.toJson(invoice);
            fileService.writeToFile(json);
            return invoice;
        } else {
            throw new DbException("ID already exist.");
        }
    }

    @Override
    public Optional<Invoice> getById(UUID id) {
        return fileService.readFile()
            .map(item -> jsonService.toObject(item, Invoice.class))
            .filter(invoice -> invoice.getId().equals(id))
            .findFirst();
    }

    @Override
    public List<Invoice> getAll() {
        return fileService.readFile()
            .map(item -> jsonService.toObject(item, Invoice.class)).collect(Collectors.toList());
    }

    @Override
    public Invoice update(UUID id, Invoice updatedInvoice) {
        delete(id);
        updatedInvoice.setId(id);
        return save(updatedInvoice);
    }

    @Override
    public void delete(UUID id) {
        List<Invoice> invoices = getAll();

        boolean isRemoved = invoices.removeIf(item -> item.getId().equals(id));

        if (!isRemoved) {
            throw new IllegalArgumentException("Id" + id + "doesn't exist.");
        }

        fileService.overwriteTheFile(invoices.stream()
            .map(item -> jsonService.toJson(item)).collect(Collectors.joining(System.lineSeparator())), StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void clear() {
        fileService.clear();
    }
}

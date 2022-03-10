package pl.futurecollars.invoicing.db.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pl.futurecollars.invoicing.db.InvoiceRepository;
import pl.futurecollars.invoicing.model.Address;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;

@AllArgsConstructor
public class SqlInvoiceRepository implements InvoiceRepository {

    public static final String SELECT_FROM_INVOICE_QUERY = """
        select 
        i.id, i.invoice_number, i.date_at, 
        s.id as seller_id, s.name as seller_name, s.tax_identifier as seller_tax_number, 
        sa.city as seller_city, sa.postal_code as seller_postal_code, sa.street_name as seller_street_name, 
        sa.street_number as seller_street_number, 
        s.pension_insurance as seller_pension_insurance, s.health_insurance as seller_health_insurance, 
        b.id as buyer_id, b.name as buyer_name, b.tax_identifier as buyer_tax_number, 
        ba.city as buyer_city, ba.postal_code as buyer_postal_code, ba.street_name as buyer_street_name, ba.street_number as buyer_street_number, 
        b.pension_insurance as buyer_pension_insurance, b.health_insurance as buyer_health_insurance 
        from invoice i 
        inner join company s on i.seller = s.id 
        inner join company b on i.buyer = b.id 
        inner join address sa on s.id = sa.company_id 
        inner join address ba on b.id = ba.company_id""";

    private final JdbcTemplate jdbcTemplate;

    private final Map<Vat, Integer> vatToId = new HashMap<>();
    private final Map<Integer, Vat> idToVat = new HashMap<>();

    @PostConstruct
    void initVatRatesMap() {
        jdbcTemplate.query("select id, name from vat", rs -> {
            Vat vat = Vat.valueOf(rs.getString("name"));
            int id = rs.getInt("id");
            vatToId.put(vat, id);
            idToVat.put(id, vat);
        });
    }

    @Override
    public Invoice save(Invoice invoice) {

        final UUID sellerId = UUID.randomUUID();
        final UUID buyerId = UUID.randomUUID();
        UUID invoiceId = invoice.getId();

        if (this.getById(invoiceId).isEmpty()) {
            jdbcTemplate.update(
                "insert into company (id, tax_identifier, name, health_insurance, pension_insurance) values (?, ?, ?, ?, ?);",
                sellerId,
                invoice.getSeller().getTaxIdentifier(),
                invoice.getSeller().getName(),
                invoice.getSeller().getPensionInsurance(),
                invoice.getSeller().getHealthInsurance()
            );

            jdbcTemplate.update(
                "insert into company (id, tax_identifier, name, health_insurance, pension_insurance) values (?, ?, ?, ?, ?);",
                buyerId,
                invoice.getBuyer().getTaxIdentifier(),
                invoice.getBuyer().getName(),
                invoice.getBuyer().getPensionInsurance(),
                invoice.getBuyer().getHealthInsurance()
            );

            jdbcTemplate.update(
                "insert into address (company_id, city, postal_code, street_name, street_number) values (?, ?, ?, ?, ?);",
                sellerId,
                invoice.getSeller().getAddress().getCity(),
                invoice.getSeller().getAddress().getPostalCode(),
                invoice.getSeller().getAddress().getStreetName(),
                invoice.getSeller().getAddress().getStreetNumber()
            );

            jdbcTemplate.update(
                "insert into address (company_id, city, postal_code, street_name, street_number) values (?, ?, ?, ?, ?);",
                buyerId,
                invoice.getBuyer().getAddress().getCity(),
                invoice.getBuyer().getAddress().getPostalCode(),
                invoice.getBuyer().getAddress().getStreetName(),
                invoice.getBuyer().getAddress().getStreetNumber()
            );

            jdbcTemplate.update(
                "insert into invoice (id, date_at, invoice_number, seller, buyer) values (?, ?, ?, ?, ?);",
                invoiceId,
                java.sql.Date.valueOf(invoice.getDateAt()),
                invoice.getNumber(),
                sellerId,
                buyerId
            );

            invoice.getEntries().forEach(entry -> {
                UUID invoiceEntryId = UUID.randomUUID();
                UUID carId = UUID.randomUUID();
                jdbcTemplate.update("insert into car (id, registration_number, personal_usage) values (?, ?, ?);",
                    carId,
                    entry.getCarRelatedExpense().getRegistrationNumber(),
                    entry.getCarRelatedExpense().isPersonalUsage()
                );
                jdbcTemplate.update("insert into invoice_entry (id, description, price, vat_value, vat_rate, car_related_expense, invoice) "
                        + "values (?, ?, ?, ?, ?, ?, ?);",
                    invoiceEntryId,
                    entry.getDescription(),
                    entry.getPrice(),
                    entry.getVatValue(),
                    vatToId.get(entry.getVatRate()),
                    carId,
                    invoiceId
                );
            });

            return invoice;
        } else {
            throw new NoSuchElementException("ID already exist.");
        }
    }

    @Override
    public Optional<Invoice> getById(UUID id) {
        // List<Invoice> invoices = jdbcTemplate.query(SELECT_FROM_INVOICE_QUERY + " where i.id = '" + id + "'", invoiceRowMapper());
        List<Invoice> invoices = jdbcTemplate.query(SELECT_FROM_INVOICE_QUERY + String.format(" where i.id = '%s'", id), invoiceRowMapper());
        return invoices.isEmpty() ? Optional.empty() : Optional.of(invoices.get(0));
    }

    @Override
    public List<Invoice> getAll() {
        return jdbcTemplate.query(SELECT_FROM_INVOICE_QUERY, invoiceRowMapper());
    }

    private RowMapper<Invoice> invoiceRowMapper() {
        return (rs, rowNr) -> {
            UUID invoiceId = UUID.fromString(rs.getString("id"));
            List<InvoiceEntry> invoiceEntries = jdbcTemplate.query(
                "select invoice_entry.id, description, price, vat_value, vat_rate, car_related_expense, "
                    + "registration_number, personal_usage from invoice_entry "
                    + "inner join invoice on invoice_entry.invoice = invoice.id "
                    + "inner join company on invoice.seller = company.id "
                    + "inner join car on invoice_entry.car_related_expense = car.id "
                    + String.format(" where invoice.id = '%s'", invoiceId),
                (response, ignored) -> InvoiceEntry.builder()
                    .id(UUID.fromString(response.getString("id")))
                    .description(response.getString("description"))
                    .price(response.getBigDecimal("price"))
                    .vatValue(response.getBigDecimal("vat_value"))
                    .vatRate(idToVat.get(response.getObject("vat_rate")))
                    .carRelatedExpense(response.getObject("registration_number") != null
                        ? Car.builder()
                        .id(UUID.fromString(response.getString("id")))
                        .registrationNumber(response.getString("registration_number"))
                        .personalUsage(response.getBoolean("personal_usage"))
                        .build()
                        : null)
                    .build());

            return Invoice.builder()
                .id(invoiceId)
                .dateAt(rs.getDate("date_at").toLocalDate())
                .number(rs.getString("invoice_number"))
                .seller(Company.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .taxIdentifier(rs.getString("seller_tax_number"))
                    .name(rs.getString("seller_name"))
                    .address(Address.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .city(rs.getString("seller_city"))
                        .postalCode(rs.getString("seller_postal_code"))
                        .streetName(rs.getString("seller_street_name"))
                        .streetNumber(rs.getString("seller_street_number"))
                        .build())
                    .healthInsurance(rs.getBigDecimal("seller_health_insurance"))
                    .pensionInsurance(rs.getBigDecimal("seller_pension_insurance"))
                    .build())
                .buyer(Company.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .taxIdentifier(rs.getString("buyer_tax_number"))
                    .name(rs.getString("buyer_name"))
                    .address(Address.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .city(rs.getString("buyer_city"))
                        .postalCode(rs.getString("buyer_postal_code"))
                        .streetName(rs.getString("buyer_street_name"))
                        .streetNumber(rs.getString("buyer_street_number"))
                        .build())
                    .healthInsurance(rs.getBigDecimal("buyer_health_insurance"))
                    .pensionInsurance(rs.getBigDecimal("buyer_pension_insurance"))
                    .build())
                .entries(invoiceEntries)
                .build();
        };
    }

    @Override
    public Invoice update(UUID id, Invoice updatedInvoice) {
        String idInsertToQuery = String.format("'%s'", id);
        if (this.getById(id).isPresent()) {
            updatedInvoice.setId(id);
            jdbcTemplate.update(
                "update company set tax_identifier = ?, name = ?, health_insurance = ?, pension_insurance = ? where id = "
                    + "(select seller from invoice where id = " + idInsertToQuery + ")",
                    updatedInvoice.getSeller().getTaxIdentifier(),
                    updatedInvoice.getSeller().getName(),
                    updatedInvoice.getSeller().getPensionInsurance(),
                    updatedInvoice.getSeller().getHealthInsurance()
            );

            jdbcTemplate.update(
                    "update company set tax_identifier = ?, name = ?, health_insurance = ?, pension_insurance = ? where id = "
                        + "(select buyer from invoice where id = " + idInsertToQuery + ")",
                    updatedInvoice.getBuyer().getTaxIdentifier(),
                    updatedInvoice.getBuyer().getName(),
                    updatedInvoice.getBuyer().getPensionInsurance(),
                    updatedInvoice.getBuyer().getHealthInsurance()
            );

            jdbcTemplate.update(
                    "update address set city = ?, postal_code = ?, street_name = ?, street_number = ? where company_id = "
                        + "(select seller from invoice where id = " + idInsertToQuery + ")",
                    updatedInvoice.getSeller().getAddress().getCity(),
                    updatedInvoice.getSeller().getAddress().getPostalCode(),
                    updatedInvoice.getSeller().getAddress().getStreetName(),
                    updatedInvoice.getSeller().getAddress().getStreetNumber()
            );

            jdbcTemplate.update(
                    "update address set city = ?, postal_code = ?, street_name = ?, street_number = ? where company_id = "
                        + "(select buyer from invoice where id = " + idInsertToQuery + ")",
                    updatedInvoice.getBuyer().getAddress().getCity(),
                    updatedInvoice.getBuyer().getAddress().getPostalCode(),
                    updatedInvoice.getBuyer().getAddress().getStreetName(),
                    updatedInvoice.getBuyer().getAddress().getStreetNumber()
            );

            jdbcTemplate.update("update invoice set date_at = ?, invoice_number = ? where id = " + idInsertToQuery,
                updatedInvoice.getDateAt(),
                updatedInvoice.getNumber()
            );

            jdbcTemplate.execute("delete from invoice_entry where invoice = " + idInsertToQuery);

            updatedInvoice.getEntries().forEach(entry -> {
                jdbcTemplate.update("update invoice_entry set description = ?, price = ?, vat_value = ?, vat_rate = ?, invoice = ?",
                    entry.getDescription(),
                    entry.getPrice(),
                    entry.getVatValue(),
                    vatToId.get(entry.getVatRate()),
                    id
                );
            });
            return updatedInvoice;
        } else {
            throw new NoSuchElementException("Invoice with id: " + id + " doesn't exist.");
        }
    }

    @Override
    public void delete(UUID id) {
        if (this.getById(id).isPresent()) {
            jdbcTemplate.execute(String.format("delete from invoice_entry where invoice_entry.invoice = '%s'", id));
            jdbcTemplate.execute(String.format("delete from invoice where id = '%s'", id));

        } else {
            throw new NoSuchElementException("Invoice with id: " + id + " doesn't exist.");
        }
    }

    @Override
    public void clear() {
        jdbcTemplate.execute("set referential_integrity false;");
        jdbcTemplate.execute("truncate table car");
        jdbcTemplate.execute("truncate table address");
        jdbcTemplate.execute("truncate table company");
        jdbcTemplate.execute("truncate table invoice_entry");
        jdbcTemplate.execute("truncate table invoice");
        jdbcTemplate.execute("set referential_integrity true;");
    }
}

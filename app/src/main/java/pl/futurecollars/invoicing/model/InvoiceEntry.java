package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceEntry {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    @Schema(description = "Invoice entry id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
    private UUID id;
    @Schema(description = "Product name", example = "Shampoo", required = true)
    private String description;
    @Schema(description = "Net price of product", example = "100.00", required = true)
    private BigDecimal price;
    @Schema(description = "Tax value of product", example = "23.00", required = true)
    private BigDecimal vatValue;
    @Column(columnDefinition = "numeric(3, 2)")
    @Schema(description = "Tax rate", required = true)
    private Vat vatRate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_related_expense")
    @Schema(description = "Car related expense")
    private Car carRelatedExpense;

    @ManyToOne(targetEntity = Invoice.class)
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonIgnore
    private Invoice invoice;
}

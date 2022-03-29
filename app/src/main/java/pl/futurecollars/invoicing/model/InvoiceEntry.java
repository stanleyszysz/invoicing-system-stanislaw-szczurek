package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceEntry {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 16, updatable = false, nullable = false)
    @Schema(description = "Invoice entry id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
    private UUID id;
    @Schema(description = "Product name", example = "Shampoo", required = true)
    private String description;
    @Schema(description = "Net price of product", example = "100.00", required = true)
    private BigDecimal price;
    @Schema(description = "Tax value of product", example = "23.00", required = true)
    private BigDecimal vatValue;
    @Schema(description = "Tax rate", required = true)
    private Vat vatRate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_related_expense")
    @Schema(description = "Car related expense")
    private Car carRelatedExpense;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Invoice invoice;
}

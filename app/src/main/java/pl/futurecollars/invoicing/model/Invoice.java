package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @Id
    @Schema(description = "Invoice id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
    private UUID id;
    @Schema(description = "Date of invoice", example = "2022-02-03", required = true)
    private LocalDate dateAt;
    @Column(name = "invoice_number")
    @Schema(description = "Invoice number", example = "2022/02/17/001", required = true)
    private String number;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller")
    @Schema(description = "Seller", required = true)
    private Company seller;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer")
    @Schema(description = "Buyer", required = true)
    private Company buyer;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "invoice")
    @Schema(description = "Product name", required = true)
    private List<InvoiceEntry> entries;

    public void generatedId() {
        this.id = UUID.randomUUID();
    }
}

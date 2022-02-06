package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @Schema(name = "Invoice id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = false)
    private UUID id;
    @Schema(name = "Date of invoice", example = "2022-02-03", required = true)
    private LocalDate dateAt;
    @Schema(name = "Seller", required = true)
    private Company seller;
    @Schema(name = "Buyer", required = true)
    private Company buyer;
    @Schema(name = "Product name", required = true)
    private List<InvoiceEntry> entries;

    public void generatedId() {
        this.id = UUID.randomUUID();
    }
}

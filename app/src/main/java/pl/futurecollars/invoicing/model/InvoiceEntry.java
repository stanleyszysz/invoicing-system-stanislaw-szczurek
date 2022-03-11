package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceEntry {

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
    @Schema(description = "Car related expense")
    private Car carRelatedExpense;
}

package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceEntry {

    @Schema(name = "Product name", example = "Shampoo", required = true)
    private String description;
    @Schema(name = "Net price of product", example = "100.00", required = true)
    private BigDecimal price;
    @Schema(name = "Tax value of product", example = "23.00", required = true)
    private BigDecimal vatValue;
    @Schema(name = "Tax rate", required = true)
    private Vat vatRate;
}

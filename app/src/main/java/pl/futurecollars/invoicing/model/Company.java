package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Schema(name = "Tax Identification Number", example = "5252287009", required = true)
    private String taxIdentifier;
    @Schema(name = "Company Name", example = "Torte", required = true)
    private String name;
    private Address address;

    @Builder.Default
    @Schema(name = "Pension insurance amount", example = "1000.00", required = true)
    private BigDecimal pensionInsurance = BigDecimal.ZERO;

    @Builder.Default
    @Schema(name = "Health insurance amount", example = "500.00", required = true)
    private BigDecimal healthInsurance = BigDecimal.ZERO;


}

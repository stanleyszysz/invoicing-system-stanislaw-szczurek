package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @Schema(description = "Company id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
    private UUID id;
    @Schema(description = "Tax Identification Number", example = "5252287009", required = true)
    private String taxIdentifier;
    @Schema(description = "Company Name", example = "Torte", required = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Address address;

    @Builder.Default
    @Schema(description = "Pension insurance amount", example = "1000.00", required = true)
    private BigDecimal pensionInsurance = BigDecimal.ZERO;

    @Builder.Default
    @Schema(description = "Health insurance amount", example = "500.00", required = true)
    private BigDecimal healthInsurance = BigDecimal.ZERO;


}

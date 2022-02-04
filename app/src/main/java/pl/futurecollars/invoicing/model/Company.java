package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Schema(name = "Tax Identification Number", example = "5252287009", required = true)
    private String taxIdentifier;
    @Schema(name = "Company Name", example = "Torte", required = true)
    private String name;
    private Address address;


}

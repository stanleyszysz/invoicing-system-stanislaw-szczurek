package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Schema(name = "City", example = "Solec", required = true)
    private String city;
    @Schema(name = "Postal Code", example = "05-532", required = true)
    private String postalCode;
    @Schema(name = "Street Name", example = "Słonecznikowa", required = true)
    private String streetName;
    @Schema(name = "Street Number", example = "8", required = true)
    private String streetNumber;

}

package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Schema(description = "Address id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
    private UUID id;
    @Schema(description = "City", example = "Solec", required = true)
    private String city;
    @Schema(description = "Postal Code", example = "05-532", required = true)
    private String postalCode;
    @Schema(description = "Street Name", example = "Słonecznikowa", required = true)
    private String streetName;
    @Schema(description = "Street Number", example = "8", required = true)
    private String streetNumber;

}

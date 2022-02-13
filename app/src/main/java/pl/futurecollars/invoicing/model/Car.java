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
public class Car {

    @Schema(name = "Car registration number", example = "DW 8G888", required = true)
    private String registrationNumber;

    @Schema(name = "Specifies car personal usage", example = "true", required = true)
    private boolean personalUsage;
}

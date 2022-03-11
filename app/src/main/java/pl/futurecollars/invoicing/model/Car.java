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
public class Car {

    @Schema(description = "Car id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
    private UUID id;
    @Schema(description = "Car registration number", example = "DW 8G888", required = true)
    private String registrationNumber;

    @Schema(description = "Specifies car personal usage", example = "true", required = true)
    private boolean personalUsage;
}

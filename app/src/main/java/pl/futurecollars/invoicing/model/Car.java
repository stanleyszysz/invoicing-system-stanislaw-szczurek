package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    @Schema(description = "Car id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
    private UUID id;
    @Schema(description = "Car registration number", example = "DW 8G888", required = true)
    private String registrationNumber;

    @Schema(description = "Specifies car personal usage", example = "true", required = true)
    private boolean personalUsage;
}

package pl.futurecollars.invoicing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
public class Address {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
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

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private Company company;

}

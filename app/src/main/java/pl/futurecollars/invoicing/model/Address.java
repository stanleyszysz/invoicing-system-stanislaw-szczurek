package pl.futurecollars.invoicing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {

    private String city;
    private String postalCode;
    private String streetName;
    private String streetNumber;

}

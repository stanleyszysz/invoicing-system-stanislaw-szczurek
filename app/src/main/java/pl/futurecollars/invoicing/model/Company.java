package pl.futurecollars.invoicing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Company {

    private String taxIdentifier;
    private String name;
    private Address address;


}

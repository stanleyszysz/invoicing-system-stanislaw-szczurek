package pl.futurecollars.invoicing.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.futurecollars.invoicing.model.TaxCalculatorResult;

public interface TaxCalculatorControllerApi {

    @GetMapping("/{taxIdentificationNumber}")
    @Operation(summary = "Tax calculation", description = "Calculation of taxes for the company")
    ResponseEntity<TaxCalculatorResult> taxCalculatorResult(@PathVariable String taxIdentifier);
}

package pl.futurecollars.invoicing.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.TaxCalculatorResult;

public interface TaxCalculatorControllerApi {

    @PostMapping
    @Operation(summary = "Tax calculation", description = "Calculation of taxes for the company")
    ResponseEntity<TaxCalculatorResult> taxCalculatorResult(@RequestBody Company company);
}

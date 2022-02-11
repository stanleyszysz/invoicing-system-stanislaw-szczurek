package pl.futurecollars.invoicing.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.TaxCalculatorResult;
import pl.futurecollars.invoicing.services.TaxCalculatorService;

@RequestMapping(path = "/tax", produces = {"application/json;charset=UTF-8"})
@RestController
@RequiredArgsConstructor
@Tag(name = "Controller for tax calculator")
public class TaxCalculatorController implements TaxCalculatorControllerApi {

    private final TaxCalculatorService taxCalculatorService;

    @Override
    public ResponseEntity<TaxCalculatorResult> taxCalculatorResult(@RequestBody Company company) {
        return ResponseEntity.ok().body(taxCalculatorService.taxCalculatorResult(company));
    }
}

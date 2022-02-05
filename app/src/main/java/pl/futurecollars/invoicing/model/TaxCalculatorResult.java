package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaxCalculatorResult {

    private final BigDecimal income;
    private final BigDecimal costs;
    private final BigDecimal earnings;

    private final BigDecimal incomingVat;
    private final BigDecimal outgoingVat;
    private final BigDecimal vatToPay;
}

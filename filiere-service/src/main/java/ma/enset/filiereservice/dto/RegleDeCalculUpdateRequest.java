package ma.enset.filiereservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record RegleDeCalculUpdateRequest(
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        BigDecimal noteValidationModule,
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        BigDecimal noteEliminatoireModule,
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        BigDecimal noteCompensationModule,
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        BigDecimal noteValidationAnnee,
        @Min(0)
        int nbrModulesDerogation
) {
}

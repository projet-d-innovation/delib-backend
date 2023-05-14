package ma.enset.filiereservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record RegleDeCalculCreationRequest(
        @NotBlank
        String codeRegleDeCalcul,
        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        BigDecimal noteValidationModule,
        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        BigDecimal noteEliminatoireModule,
        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        BigDecimal noteCompensationModule,
        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        BigDecimal noteValidationAnnee,
        @NotNull
        @Min(0)
        int nbrModulesDerogation
) {
}

package ma.enset.filiereservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RegleDeCalculRequestDTO(
        @NotBlank
        String codeRegle,

        @DecimalMin("0.0")
        @DecimalMax("20.0")
        float noteValidationModule,
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        float noteEliminatoireModule,
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        float noteCompensationModule,
        @DecimalMin("0.0")
        @DecimalMax("20.0")
        float noteValidationAnnee,
        @Min(0)
        int nbrModulesDerogation
) {
}
